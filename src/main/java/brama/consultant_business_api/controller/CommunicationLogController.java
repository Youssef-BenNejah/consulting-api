package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogCreateRequest;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogUpdateRequest;
import brama.consultant_business_api.domain.communication.dto.response.CommunicationLogResponse;
import brama.consultant_business_api.service.communication.CommunicationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/communication-logs")
@RequiredArgsConstructor
@Tag(name = "Communication Logs", description = "Communication log API")
public class CommunicationLogController {
    private final CommunicationLogService service;

    @GetMapping
    public ApiResponse<List<CommunicationLogResponse>> listLogs(
            @RequestParam(required = false) final String clientId,
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateTo,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<CommunicationLogResponse> result = service.search(clientId, projectId, dateFrom, dateTo, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommunicationLogResponse> createLog(@Valid @RequestBody final CommunicationLogCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<CommunicationLogResponse> updateLog(@PathVariable final String id,
                                                           @Valid @RequestBody final CommunicationLogUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLog(@PathVariable final String id) {
        service.delete(id);
    }
}
