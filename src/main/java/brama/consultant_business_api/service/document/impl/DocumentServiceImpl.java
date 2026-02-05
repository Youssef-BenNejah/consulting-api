package brama.consultant_business_api.service.document.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.document.dto.request.DocumentCreateRequest;
import brama.consultant_business_api.domain.document.dto.response.DocumentDownload;
import brama.consultant_business_api.domain.document.dto.response.DocumentResponse;
import brama.consultant_business_api.config.CloudinaryProperties;
import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.domain.document.enums.DocumentEntityType;
import brama.consultant_business_api.domain.document.mapper.DocumentMapper;
import brama.consultant_business_api.domain.document.model.DocumentFile;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.exception.ErrorCode;
import brama.consultant_business_api.repository.DocumentRepository;
import brama.consultant_business_api.service.document.DocumentService;
import brama.consultant_business_api.service.storage.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository repository;
    private final MongoTemplate mongoTemplate;
    private final DocumentMapper mapper;
    private final GridFsTemplate gridFsTemplate;
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;

    @Override
    public PagedResult<DocumentResponse> search(final String search,
                                                final DocumentCategoryKey category,
                                                final String clientId,
                                                final String projectId,
                                                final LocalDate dateFrom,
                                                final LocalDate dateTo,
                                                final Integer page,
                                                final Integer size) {
        final Query query = new Query();
        QueryUtils.addRegexOrCriteria(query, search, "name", "clientName", "projectName");
        QueryUtils.addIfNotNull(query, "category", category);
        QueryUtils.addIfNotBlank(query, "clientId", clientId);
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addDateRange(query, "uploadedAt", dateFrom, dateTo);
        final long total = mongoTemplate.count(query, DocumentFile.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<DocumentResponse> items = mongoTemplate.find(query, DocumentFile.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<DocumentResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public DocumentResponse createMetadata(final DocumentCreateRequest request) {
        final DocumentFile document = mapper.toEntity(request);
        applyEntityReference(document, request);
        final DocumentFile saved = repository.save(document);
        return mapper.toResponse(saved);
    }

    @Override
    public DocumentResponse upload(final MultipartFile file, final DocumentCreateRequest request) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "file is required");
        }
        final String filename = StringUtils.hasText(request.getName()) ? request.getName() : file.getOriginalFilename();
        final String contentType = file.getContentType();
        try {
            final DocumentFile document = mapper.toEntity(request);
            applyEntityReference(document, request);
            document.setName(filename);
            document.setUploadedAt(request.getUploadedAt() != null ? request.getUploadedAt() : LocalDate.now());
            document.setSize(formatSize(file.getSize()));
            document.setFileType(extractFileType(filename, contentType));
            if (cloudinaryService.isEnabled()) {
                final String folder = buildCloudinaryFolder(document);
                final CloudinaryService.CloudinaryUploadResult uploaded = cloudinaryService.upload(file, folder);
                document.setFileId(uploaded.publicId());
                document.setFileUrl(uploaded.secureUrl());
                document.setResourceType(uploaded.resourceType());
                document.setStorageProvider("cloudinary");
            } else {
                final ObjectId fileId = gridFsTemplate.store(file.getInputStream(), filename, contentType);
                document.setFileId(fileId.toHexString());
                document.setStorageProvider("gridfs");
            }
            final DocumentFile saved = repository.save(document);
            return mapper.toResponse(saved);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.INTERNAL_EXCEPTION, ex.getMessage());
        }
    }

    @Override
    public DocumentResponse getById(final String id) {
        final DocumentFile document = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));
        return mapper.toResponse(document);
    }

    @Override
    public void delete(final String id) {
        final DocumentFile document = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));
        repository.deleteById(id);
        if ("cloudinary".equalsIgnoreCase(document.getStorageProvider()) && document.getFileId() != null) {
            cloudinaryService.delete(document.getFileId(), document.getResourceType());
            return;
        }
        if (document.getFileId() != null) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(document.getFileId()))));
        }
    }

    @Override
    public DocumentDownload download(final String id) {
        final DocumentFile document = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));
        if ("cloudinary".equalsIgnoreCase(document.getStorageProvider()) && document.getFileUrl() != null) {
            final byte[] content = cloudinaryService.download(document.getFileUrl());
            if (content == null) {
                throw new EntityNotFoundException("Document file not found: " + id);
            }
            return DocumentDownload.builder()
                    .content(content)
                    .filename(document.getName())
                    .contentType("application/octet-stream")
                    .build();
        }
        if (document.getFileId() == null) {
            throw new EntityNotFoundException("Document file not found: " + id);
        }
        final GridFSFile gridFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(document.getFileId()))));
        if (gridFile == null) {
            throw new EntityNotFoundException("Document file not found: " + id);
        }
        final GridFsResource resource = gridFsTemplate.getResource(gridFile);
        try (InputStream in = resource.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            in.transferTo(baos);
            return DocumentDownload.builder()
                    .content(baos.toByteArray())
                    .filename(document.getName())
                    .contentType(resource.getContentType())
                    .build();
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.INTERNAL_EXCEPTION, ex.getMessage());
        }
    }

    private String extractFileType(final String filename, final String contentType) {
        if (filename != null && filename.contains(".")) {
            final String ext = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
            return ext;
        }
        if (contentType != null && contentType.contains("/")) {
            return contentType.substring(contentType.indexOf('/') + 1).toUpperCase();
        }
        return "FILE";
    }

    private String formatSize(final long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        final double kb = bytes / 1024.0;
        if (kb < 1024) {
            return String.format("%.1f KB", kb);
        }
        final double mb = kb / 1024.0;
        if (mb < 1024) {
            return String.format("%.1f MB", mb);
        }
        final double gb = mb / 1024.0;
        return String.format("%.1f GB", gb);
    }

    private void applyEntityReference(final DocumentFile document, final DocumentCreateRequest request) {
        final String explicitEntityId = StringUtils.hasText(request.getEntityId()) ? request.getEntityId() : null;
        final String projectId = StringUtils.hasText(request.getProjectId()) ? request.getProjectId() : null;
        final String clientId = StringUtils.hasText(request.getClientId()) ? request.getClientId() : null;

        final DocumentEntityType entityType = request.getEntityType() != null
                ? request.getEntityType()
                : (projectId != null ? DocumentEntityType.PROJECT
                : (clientId != null ? DocumentEntityType.CLIENT
                : (request.getCategory() == DocumentCategoryKey.INVOICE ? DocumentEntityType.INVOICE : DocumentEntityType.DOCUMENT)));

        final String entityId = explicitEntityId != null ? explicitEntityId : (projectId != null ? projectId : clientId);

        if (!StringUtils.hasText(entityId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "entityId or projectId/clientId is required");
        }

        document.setEntityType(entityType);
        document.setEntityId(entityId);
    }

    private String buildCloudinaryFolder(final DocumentFile document) {
        final String base = StringUtils.hasText(cloudinaryProperties.getFolder())
                ? cloudinaryProperties.getFolder()
                : "consulting-cherif";
        final String typeFolder = document.getEntityType() != null
                ? document.getEntityType().getFolderName()
                : "documents";
        final String entityId = sanitizeFolderSegment(document.getEntityId());
        return String.join("/", base, typeFolder, entityId);
    }

    private String sanitizeFolderSegment(final String value) {
        if (!StringUtils.hasText(value)) {
            return "unknown";
        }
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_-]", "-");
    }
}
