package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.risk.dto.request.RiskCreateRequest;
import brama.consultant_business_api.domain.risk.dto.request.RiskUpdateRequest;
import brama.consultant_business_api.domain.risk.dto.response.RiskResponse;
import brama.consultant_business_api.domain.risk.enums.RiskStatus;
import brama.consultant_business_api.service.risk.RiskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/risks")
@RequiredArgsConstructor
@Tag(name = "Risks", description = "Risk API")
public class RiskController {
    private final RiskService service;

    @GetMapping
    public ApiResponse<List<RiskResponse>> listRisks(
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final RiskStatus status,
            @RequestParam(required = false) final Double minScore,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<RiskResponse> result = service.search(projectId, status, minScore, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RiskResponse> createRisk(@Valid @RequestBody final RiskCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<RiskResponse> updateRisk(@PathVariable final String id,
                                                @Valid @RequestBody final RiskUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRisk(@PathVariable final String id) {
        service.delete(id);
    }
}
