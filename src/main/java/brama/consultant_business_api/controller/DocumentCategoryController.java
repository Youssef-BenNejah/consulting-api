package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryUpdateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.response.DocumentCategoryResponse;
import brama.consultant_business_api.service.documentcategory.DocumentCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document-categories")
@RequiredArgsConstructor
@Tag(name = "Document Categories", description = "Document category API")
public class DocumentCategoryController {
    private final DocumentCategoryService service;

    @GetMapping
    public ApiResponse<List<DocumentCategoryResponse>> listCategories() {
        return ApiResponse.ok(service.list());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DocumentCategoryResponse> createCategory(@Valid @RequestBody final DocumentCategoryCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<DocumentCategoryResponse> updateCategory(@PathVariable final String id,
                                                                @Valid @RequestBody final DocumentCategoryUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable final String id) {
        service.delete(id);
    }
}
