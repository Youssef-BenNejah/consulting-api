package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.domain.report.dto.response.InvoicesReportResponse;
import brama.consultant_business_api.domain.report.dto.response.MarginsReportResponse;
import brama.consultant_business_api.domain.report.dto.response.ProjectsReportResponse;
import brama.consultant_business_api.domain.report.dto.response.ReportsOverviewResponse;
import brama.consultant_business_api.service.report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Reporting API")
public class ReportsController {
    private final ReportService service;

    @GetMapping("/overview")
    public ApiResponse<ReportsOverviewResponse> getOverview() {
        return ApiResponse.ok(service.getOverview());
    }

    @GetMapping("/projects")
    public ApiResponse<ProjectsReportResponse> getProjectsReport() {
        return ApiResponse.ok(service.getProjectsReport());
    }

    @GetMapping("/margins")
    public ApiResponse<MarginsReportResponse> getMarginsReport() {
        return ApiResponse.ok(service.getMarginsReport());
    }

    @GetMapping("/invoices")
    public ApiResponse<InvoicesReportResponse> getInvoicesReport() {
        return ApiResponse.ok(service.getInvoicesReport());
    }
}
