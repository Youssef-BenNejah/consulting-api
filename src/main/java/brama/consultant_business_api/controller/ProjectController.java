package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.client.dto.response.ClientResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.FinancialsResponse;
import brama.consultant_business_api.domain.document.dto.response.DocumentResponse;
import brama.consultant_business_api.domain.invoice.dto.response.InvoiceResponse;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.schedule.dto.response.ScheduleResponse;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.service.client.ClientService;
import brama.consultant_business_api.service.document.DocumentService;
import brama.consultant_business_api.service.invoice.InvoiceService;
import brama.consultant_business_api.service.project.ProjectService;
import brama.consultant_business_api.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project API")
public class ProjectController {
    private final ProjectService service;
    private final ClientService clientService;
    private final DocumentService documentService;
    private final ScheduleService scheduleService;
    private final InvoiceService invoiceService;

    @GetMapping
    public ApiResponse<List<ProjectResponse>> listProjects(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false) final String statusId,
            @RequestParam(required = false) final String clientId,
            @RequestParam(required = false) final String projectTypeId,
            @RequestParam(required = false) final String priorityId,
            @RequestParam(required = false) final String tagId,
            @RequestParam(required = false) final HealthStatus healthStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateTo,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String sort) {
        final PagedResult<ProjectResponse> result = service.search(
                search, statusId, clientId, projectTypeId, priorityId, tagId,
                healthStatus, dateFrom, dateTo, page, size, sort);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjectResponse> createProject(@Valid @RequestBody final ProjectCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> getProject(@PathVariable final String id) {
        return ApiResponse.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable final String id,
                                                      @Valid @RequestBody final ProjectUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable final String id) {
        service.delete(id);
    }

    @GetMapping("/{projectId}/client")
    public ApiResponse<ClientResponse> getClientByProjectId(@PathVariable final String projectId) {
        final ProjectResponse project = service.getById(projectId);
        if (project.getClientId() == null || project.getClientId().isBlank()) {
            throw new EntityNotFoundException("Client not found for project: " + projectId);
        }
        return ApiResponse.ok(clientService.getById(project.getClientId()));
    }

    @GetMapping("/{projectId}/documents")
    public ApiResponse<List<DocumentResponse>> getDocumentsByProjectId(@PathVariable final String projectId,
                                                                       @RequestParam(required = false) final Integer page,
                                                                       @RequestParam(required = false) final Integer size) {
        final PagedResult<DocumentResponse> result = documentService.search(
                null, null, null, projectId, null, null, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @GetMapping("/{projectId}/planning")
    public ApiResponse<List<ScheduleResponse>> getPlanningByProjectId(@PathVariable final String projectId,
                                                                      @RequestParam(required = false) final Integer page,
                                                                      @RequestParam(required = false) final Integer size) {
        final PagedResult<ScheduleResponse> result = scheduleService.search(
                null, null, projectId, null, null, null, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @GetMapping("/{projectId}/invoices")
    public ApiResponse<List<InvoiceResponse>> getInvoicesByProjectId(@PathVariable final String projectId,
                                                                     @RequestParam(required = false) final Integer page,
                                                                     @RequestParam(required = false) final Integer size,
                                                                     @RequestParam(required = false) final String sort) {
        final PagedResult<InvoiceResponse> result = invoiceService.search(
                null, null, null, projectId, null, null, page, size, sort);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @GetMapping("/{projectId}/finances")
    public ApiResponse<FinancialsResponse> getFinancesByProjectId(@PathVariable final String projectId) {
        return ApiResponse.ok(invoiceService.getProjectFinancials(projectId));
    }
}
