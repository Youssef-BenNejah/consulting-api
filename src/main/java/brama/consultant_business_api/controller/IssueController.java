package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.issue.dto.request.IssueCreateRequest;
import brama.consultant_business_api.domain.issue.dto.request.IssueUpdateRequest;
import brama.consultant_business_api.domain.issue.dto.response.IssueResponse;
import brama.consultant_business_api.domain.issue.enums.IssueSeverity;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
import brama.consultant_business_api.service.issue.IssueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
@Tag(name = "Issues", description = "Issue API")
public class IssueController {
    private final IssueService service;

    @GetMapping
    public ApiResponse<List<IssueResponse>> listIssues(
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final IssueStatus status,
            @RequestParam(required = false) final IssueSeverity severity,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<IssueResponse> result = service.search(projectId, status, severity, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<IssueResponse> createIssue(@Valid @RequestBody final IssueCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<IssueResponse> updateIssue(@PathVariable final String id,
                                                  @Valid @RequestBody final IssueUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable final String id) {
        service.delete(id);
    }
}
