package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneCreateRequest;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneUpdateRequest;
import brama.consultant_business_api.domain.milestone.dto.response.MilestoneResponse;
import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
import brama.consultant_business_api.service.milestone.MilestoneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/milestones")
@RequiredArgsConstructor
@Tag(name = "Milestones", description = "Milestone API")
public class MilestoneController {
    private final MilestoneService service;

    @GetMapping
    public ApiResponse<List<MilestoneResponse>> listMilestones(
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final MilestoneStatus status,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<MilestoneResponse> result = service.search(projectId, status, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MilestoneResponse> createMilestone(@Valid @RequestBody final MilestoneCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<MilestoneResponse> updateMilestone(@PathVariable final String id,
                                                          @Valid @RequestBody final MilestoneUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMilestone(@PathVariable final String id) {
        service.delete(id);
    }
}
