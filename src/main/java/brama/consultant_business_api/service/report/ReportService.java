package brama.consultant_business_api.service.report;

import brama.consultant_business_api.domain.report.dto.response.InvoicesReportResponse;
import brama.consultant_business_api.domain.report.dto.response.MarginsReportResponse;
import brama.consultant_business_api.domain.report.dto.response.ProjectsReportResponse;
import brama.consultant_business_api.domain.report.dto.response.ReportsOverviewResponse;

public interface ReportService {
    ReportsOverviewResponse getOverview();

    ProjectsReportResponse getProjectsReport();

    MarginsReportResponse getMarginsReport();

    InvoicesReportResponse getInvoicesReport();
}
