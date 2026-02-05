package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityCreateRequest;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityUpdateRequest;
import brama.consultant_business_api.domain.opportunity.dto.response.OpportunityResponse;
import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;
import brama.consultant_business_api.service.opportunity.OpportunityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/opportunities")
@RequiredArgsConstructor
@Tag(name = "Opportunities", description = "Opportunity API")
public class OpportunityController {
    private final OpportunityService service;

    @GetMapping
    public ApiResponse<List<OpportunityResponse>> listOpportunities(
            @RequestParam(required = false) final String clientId,
            @RequestParam(required = false) final OpportunityStage stage,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate expectedCloseFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate expectedCloseTo,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<OpportunityResponse> result = service.search(clientId, stage, expectedCloseFrom, expectedCloseTo, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OpportunityResponse> createOpportunity(@Valid @RequestBody final OpportunityCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<OpportunityResponse> updateOpportunity(@PathVariable final String id,
                                                              @Valid @RequestBody final OpportunityUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOpportunity(@PathVariable final String id) {
        service.delete(id);
    }
}
