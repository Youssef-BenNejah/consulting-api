package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.document.dto.request.DocumentCreateRequest;
import brama.consultant_business_api.domain.document.dto.response.DocumentDownload;
import brama.consultant_business_api.domain.document.dto.response.DocumentResponse;
import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.service.document.DocumentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document API")
public class DocumentController {
    private final DocumentService service;

    @GetMapping
    public ApiResponse<List<DocumentResponse>> listDocuments(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false) final DocumentCategoryKey category,
            @RequestParam(required = false) final String clientId,
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateTo,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<DocumentResponse> result = service.search(
                search, category, clientId, projectId, dateFrom, dateTo, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DocumentResponse> createDocument(@Valid @RequestBody final DocumentCreateRequest request) {
        return ApiResponse.ok(service.createMetadata(request));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DocumentResponse> uploadDocument(@RequestPart("file") final MultipartFile file,
                                                        @Valid @ModelAttribute final DocumentCreateRequest request) {
        return ApiResponse.ok(service.upload(file, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<DocumentResponse> getDocument(@PathVariable final String id) {
        return ApiResponse.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable final String id) {
        service.delete(id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable final String id) {
        final DocumentDownload download = service.download(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download.getFilename() + "\"")
                .contentType(download.getContentType() != null ? MediaType.parseMediaType(download.getContentType()) : MediaType.APPLICATION_OCTET_STREAM)
                .body(download.getContent());
    }
}
