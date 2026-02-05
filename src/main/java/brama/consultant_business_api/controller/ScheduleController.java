package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;
import brama.consultant_business_api.domain.schedule.dto.response.ScheduleResponse;
import brama.consultant_business_api.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Project schedule API")
public class ScheduleController {
    private final ScheduleService service;

    @GetMapping
    public ApiResponse<List<ScheduleResponse>> listSchedules(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false) final String clientId,
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final ProjectStatus projectStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateTo,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<ScheduleResponse> result = service.search(
                search, clientId, projectId, projectStatus, dateFrom, dateTo, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ScheduleResponse> createSchedule(@Valid @RequestBody final ScheduleCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<ScheduleResponse> getSchedule(@PathVariable final String id) {
        return ApiResponse.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ScheduleResponse> updateSchedule(@PathVariable final String id,
                                                        @Valid @RequestBody final ScheduleUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchedule(@PathVariable final String id) {
        service.delete(id);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportSchedules(
            @RequestParam(defaultValue = "csv") final String format,
            @RequestParam(required = false) final String search,
            @RequestParam(required = false) final String clientId,
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final ProjectStatus projectStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateTo) {
        if (!"csv".equalsIgnoreCase(format)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        final byte[] csv = service.exportCsv(search, clientId, projectId, projectStatus, dateFrom, dateTo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"schedules.csv\"")
                .contentType(MediaType.valueOf("text/csv"))
                .body(csv);
    }
}
