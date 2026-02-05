package brama.consultant_business_api.service.documentcategory;

import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryUpdateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.response.DocumentCategoryResponse;

import java.util.List;

public interface DocumentCategoryService {
    List<DocumentCategoryResponse> list();

    DocumentCategoryResponse create(DocumentCategoryCreateRequest request);

    DocumentCategoryResponse getById(String id);

    DocumentCategoryResponse update(String id, DocumentCategoryUpdateRequest request);

    void delete(String id);
}
