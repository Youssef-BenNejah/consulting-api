package brama.consultant_business_api.domain.document.mapper;

import brama.consultant_business_api.domain.document.dto.request.DocumentCreateRequest;
import brama.consultant_business_api.domain.document.dto.response.DocumentResponse;
import brama.consultant_business_api.domain.document.model.DocumentFile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DocumentMapper {
    public DocumentFile toEntity(final DocumentCreateRequest request) {
        if (request == null) {
            return null;
        }
        return DocumentFile.builder()
                .name(request.getName())
                .category(request.getCategory())
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .uploadedBy(request.getUploadedBy())
                .uploadedAt(request.getUploadedAt() != null ? request.getUploadedAt() : LocalDate.now())
                .size(request.getSize())
                .fileType(request.getFileType())
                .build();
    }

    public DocumentResponse toResponse(final DocumentFile document) {
        if (document == null) {
            return null;
        }
        return DocumentResponse.builder()
                .id(document.getId())
                .name(document.getName())
                .category(document.getCategory())
                .clientId(document.getClientId())
                .clientName(document.getClientName())
                .projectId(document.getProjectId())
                .projectName(document.getProjectName())
                .entityType(document.getEntityType())
                .entityId(document.getEntityId())
                .uploadedBy(document.getUploadedBy())
                .uploadedAt(document.getUploadedAt())
                .size(document.getSize())
                .fileType(document.getFileType())
                .fileId(document.getFileId())
                .fileUrl(document.getFileUrl())
                .storageProvider(document.getStorageProvider())
                .resourceType(document.getResourceType())
                .build();
    }
}
