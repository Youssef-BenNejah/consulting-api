package brama.consultant_business_api.service.documentcategory.impl;

import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryUpdateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.response.DocumentCategoryResponse;
import brama.consultant_business_api.domain.documentcategory.mapper.DocumentCategoryMapper;
import brama.consultant_business_api.domain.documentcategory.model.DocumentCategory;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.DocumentCategoryRepository;
import brama.consultant_business_api.service.documentcategory.DocumentCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentCategoryServiceImpl implements DocumentCategoryService {
    private final DocumentCategoryRepository repository;
    private final DocumentCategoryMapper mapper;

    @Override
    public List<DocumentCategoryResponse> list() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentCategoryResponse create(final DocumentCategoryCreateRequest request) {
        final DocumentCategory category = mapper.toEntity(request);
        final DocumentCategory saved = repository.save(category);
        return mapper.toResponse(saved);
    }

    @Override
    public DocumentCategoryResponse getById(final String id) {
        final DocumentCategory category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document category not found: " + id));
        return mapper.toResponse(category);
    }

    @Override
    public DocumentCategoryResponse update(final String id, final DocumentCategoryUpdateRequest request) {
        final DocumentCategory category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document category not found: " + id));
        mapper.merge(category, request);
        final DocumentCategory saved = repository.save(category);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Document category not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
