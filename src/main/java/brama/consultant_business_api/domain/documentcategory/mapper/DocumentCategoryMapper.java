package brama.consultant_business_api.domain.documentcategory.mapper;

import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryUpdateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.response.DocumentCategoryResponse;
import brama.consultant_business_api.domain.documentcategory.model.DocumentCategory;
import org.springframework.stereotype.Component;

@Component
public class DocumentCategoryMapper {
    public DocumentCategory toEntity(final DocumentCategoryCreateRequest request) {
        if (request == null) {
            return null;
        }
        return DocumentCategory.builder()
                .name(request.getName())
                .key(request.getKey())
                .color(request.getColor())
                .build();
    }

    public void merge(final DocumentCategory category, final DocumentCategoryUpdateRequest request) {
        if (category == null || request == null) {
            return;
        }
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getKey() != null) {
            category.setKey(request.getKey());
        }
        if (request.getColor() != null) {
            category.setColor(request.getColor());
        }
    }

    public DocumentCategoryResponse toResponse(final DocumentCategory category) {
        if (category == null) {
            return null;
        }
        return DocumentCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .key(category.getKey())
                .color(category.getColor())
                .build();
    }
}
