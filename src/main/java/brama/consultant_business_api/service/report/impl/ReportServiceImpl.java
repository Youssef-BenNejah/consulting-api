package brama.consultant_business_api.service.report.impl;

import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
import brama.consultant_business_api.domain.project.mapper.ProjectMapper;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.domain.report.dto.response.InvoicesReportResponse;
import brama.consultant_business_api.domain.report.dto.response.MarginsReportResponse;
import brama.consultant_business_api.domain.report.dto.response.ProjectMarginItemResponse;
import brama.consultant_business_api.domain.report.dto.response.ProjectStatusCountsResponse;
import brama.consultant_business_api.domain.report.dto.response.ProjectsReportResponse;
import brama.consultant_business_api.domain.report.dto.response.ReportsOverviewResponse;
import brama.consultant_business_api.domain.invoice.dto.response.InvoiceResponse;
import brama.consultant_business_api.domain.invoice.mapper.InvoiceMapper;
import brama.consultant_business_api.domain.settings.catalog.SettingsCatalogDefaults;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsProjectStatus;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.repository.SettingsCatalogRepository;
import brama.consultant_business_api.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ProjectRepository projectRepository;
    private final InvoiceRepository invoiceRepository;
    private final ProjectMapper projectMapper;
    private final InvoiceMapper invoiceMapper;
    private final SettingsCatalogRepository settingsCatalogRepository;

    @Override
    public ReportsOverviewResponse getOverview() {
        final List<Project> projects = projectRepository.findAll();
        final double totalRevenue = safeSum(projects.stream()
                .mapToDouble(p -> p.getClientBudget() != null ? p.getClientBudget() : 0D));
        final double totalCost = safeSum(projects.stream()
                .mapToDouble(p -> (p.getVendorCost() != null ? p.getVendorCost() : 0D)
                        + (p.getInternalCost() != null ? p.getInternalCost() : 0D)));
        final double totalMargin = clamp(totalRevenue - totalCost);
        final double avgMarginPercent = projects.isEmpty() ? 0D : clamp(projects.stream()
                .filter(p -> p.getClientBudget() != null && p.getClientBudget() > 0)
                .mapToDouble(p -> ((p.getClientBudget() - (p.getVendorCost() != null ? p.getVendorCost() : 0D)
                        - (p.getInternalCost() != null ? p.getInternalCost() : 0D)) / p.getClientBudget()) * 100D)
                .average()
                .orElse(0D));

        final List<Invoice> invoices = invoiceRepository.findAll();
        final double totalReceivables = safeSum(invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.SENT || i.getStatus() == InvoiceStatus.OVERDUE)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
        );
        final long overdueInvoices = invoices.stream().filter(i -> i.getStatus() == InvoiceStatus.OVERDUE).count();
        final long totalInvoices = invoices.size();

        return ReportsOverviewResponse.builder()
                .totalRevenue(totalRevenue)
                .totalCost(totalCost)
                .totalMargin(totalMargin)
                .avgMarginPercent(avgMarginPercent)
                .totalReceivables(totalReceivables)
                .overdueInvoices(overdueInvoices)
                .totalInvoices(totalInvoices)
                .build();
    }

    @Override
    public ProjectsReportResponse getProjectsReport() {
        final List<Project> projects = projectRepository.findAll();
        final SettingsCatalog catalog = settingsCatalogRepository.findById(SettingsCatalogDefaults.CATALOG_ID)
                .orElseGet(SettingsCatalogDefaults::defaultCatalog);
        final String deliveryId = resolveStatusIdByKey(catalog, "delivery");
        final String discoveryId = resolveStatusIdByKey(catalog, "discovery");
        final String reviewId = resolveStatusIdByKey(catalog, "review");
        final String closedId = resolveStatusIdByKey(catalog, "closed");
        final ProjectStatusCountsResponse counts = ProjectStatusCountsResponse.builder()
                .delivery(projects.stream().filter(p -> matchesStatus(p.getStatusId(), deliveryId)).count())
                .discovery(projects.stream().filter(p -> matchesStatus(p.getStatusId(), discoveryId)).count())
                .review(projects.stream().filter(p -> matchesStatus(p.getStatusId(), reviewId)).count())
                .closed(projects.stream().filter(p -> matchesStatus(p.getStatusId(), closedId)).count())
                .other(projects.stream().filter(p -> !matchesStatus(p.getStatusId(), deliveryId)
                        && !matchesStatus(p.getStatusId(), discoveryId)
                        && !matchesStatus(p.getStatusId(), reviewId)
                        && !matchesStatus(p.getStatusId(), closedId)).count())
                .build();

        final List<ProjectResponse> projectResponses = projects.stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());

        return ProjectsReportResponse.builder()
                .statusCounts(counts)
                .projects(projectResponses)
                .build();
    }

    @Override
    public MarginsReportResponse getMarginsReport() {
        final List<Project> projects = projectRepository.findAll();
        final List<ProjectMarginItemResponse> items = projects.stream()
                .map(project -> {
                    final double budget = project.getClientBudget() != null ? project.getClientBudget() : 0D;
                    final double vendor = project.getVendorCost() != null ? project.getVendorCost() : 0D;
                    final double internal = project.getInternalCost() != null ? project.getInternalCost() : 0D;
                    final double margin = clamp(budget - vendor - internal);
                    final double marginPercent = budget > 0 ? clamp((margin / budget) * 100D) : 0D;
                    return ProjectMarginItemResponse.builder()
                            .id(project.getId())
                            .name(project.getName())
                            .clientName(project.getClientName())
                            .clientBudget(budget)
                            .vendorCost(vendor)
                            .internalCost(internal)
                            .margin(margin)
                            .marginPercent(marginPercent)
                            .build();
                })
                .sorted(Comparator.comparingDouble(ProjectMarginItemResponse::getMarginPercent).reversed())
                .collect(Collectors.toList());

        final double totalRevenue = safeSum(items.stream().mapToDouble(ProjectMarginItemResponse::getClientBudget));
        final double totalCost = safeSum(items.stream().mapToDouble(i -> i.getVendorCost() + i.getInternalCost()));
        final double totalMargin = clamp(totalRevenue - totalCost);
        final double avgMarginPercent = items.isEmpty() ? 0D : clamp(items.stream()
                .mapToDouble(ProjectMarginItemResponse::getMarginPercent)
                .average()
                .orElse(0D));

        return MarginsReportResponse.builder()
                .totalRevenue(totalRevenue)
                .totalCost(totalCost)
                .totalMargin(totalMargin)
                .avgMarginPercent(avgMarginPercent)
                .projects(items)
                .build();
    }

    @Override
    public InvoicesReportResponse getInvoicesReport() {
        final List<Invoice> invoices = invoiceRepository.findAll();
        final double totalReceivables = safeSum(invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.SENT || i.getStatus() == InvoiceStatus.OVERDUE)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
        );
        final long overdueInvoices = invoices.stream().filter(i -> i.getStatus() == InvoiceStatus.OVERDUE).count();
        final long totalInvoices = invoices.size();
        final List<InvoiceResponse> overdue = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OVERDUE)
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());

        return InvoicesReportResponse.builder()
                .totalReceivables(totalReceivables)
                .overdueInvoices(overdueInvoices)
                .totalInvoices(totalInvoices)
                .overdue(overdue)
                .build();
    }

    private double safeSum(final DoubleStream stream) {
        return clamp(stream.sum());
    }

    private double clamp(final double value) {
        if (Double.isNaN(value)) {
            return 0D;
        }
        if (Double.isInfinite(value)) {
            return value > 0 ? Double.MAX_VALUE : -Double.MAX_VALUE;
        }
        return value;
    }

    private String resolveStatusIdByKey(final SettingsCatalog catalog, final String key) {
        if (catalog == null || catalog.getProjectStatuses() == null || key == null) {
            return null;
        }
        for (SettingsProjectStatus status : catalog.getProjectStatuses()) {
            if (status != null && status.getKey() != null && status.getKey().equalsIgnoreCase(key)) {
                return status.getId();
            }
        }
        return null;
    }

    private boolean matchesStatus(final String statusId, final String targetId) {
        return statusId != null && targetId != null && statusId.equals(targetId);
    }
}
