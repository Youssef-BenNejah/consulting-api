package brama.consultant_business_api.service.dashboard.impl;

import brama.consultant_business_api.domain.dashboard.dto.response.CostsSummaryResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.CriticalProjectResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardFinancialsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardNotification;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardNotificationsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardProjectHealthResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardStatsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardSummaryResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.FinancialsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.PipelineResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.ProjectHealthDistributionResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.ProjectsByHealthResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.RevenueSummaryResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.UpcomingScheduleItemResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.UpcomingSchedulesResponse;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.domain.schedule.model.ProjectSchedule;
import brama.consultant_business_api.domain.settings.catalog.SettingsCatalogDefaults;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsProjectStatus;
import brama.consultant_business_api.repository.ClientRepository;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.repository.SettingsCatalogRepository;
import brama.consultant_business_api.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private static final Duration SUMMARY_CACHE_TTL = Duration.ofSeconds(10);
    private static final int MAX_NOTIFICATIONS = 6;
    private static final String CURRENCY_EUR = "EUR";

    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;
    private final InvoiceRepository invoiceRepository;
    private final SettingsCatalogRepository settingsCatalogRepository;
    private final MongoTemplate mongoTemplate;
    private final AtomicReference<CachedSummary> summaryCache = new AtomicReference<>();

    @Override
    public DashboardSummaryResponse getSummary() {
        final Instant now = Instant.now();
        final CachedSummary cached = summaryCache.get();
        if (cached != null && cached.cachedAt.plus(SUMMARY_CACHE_TTL).isAfter(now)) {
            return cached.summary;
        }
        final long totalClients = clientRepository.count();

        final List<Project> projects = projectRepository.findAll();
        final SettingsCatalog catalog = settingsCatalogRepository.findById(SettingsCatalogDefaults.CATALOG_ID)
                .orElseGet(SettingsCatalogDefaults::defaultCatalog);
        final ActiveProjectFilters activeFilters = resolveActiveFilters(catalog);
        final long activeProjects = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .count();
        final long green = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .filter(p -> p.getHealthStatus() == HealthStatus.GREEN)
                .count();
        final long amber = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .filter(p -> p.getHealthStatus() == HealthStatus.AMBER)
                .count();
        final long red = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .filter(p -> p.getHealthStatus() == HealthStatus.RED)
                .count();

        final List<Invoice> invoices = invoiceRepository.findAll();
        final double totalBilled = invoices.stream().mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D).sum();
        final double totalReceived = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();
        final double outstandingReceivables = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.SENT || i.getStatus() == InvoiceStatus.OVERDUE)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();

        final DashboardSummaryResponse summary = DashboardSummaryResponse.builder()
                .totalClients(totalClients)
                .activeProjects(activeProjects)
                .projectsByHealth(ProjectsByHealthResponse.builder()
                        .green(green)
                        .amber(amber)
                        .red(red)
                        .build())
                .financials(FinancialsResponse.builder()
                        .totalBilled(totalBilled)
                        .totalReceived(totalReceived)
                        .outstandingReceivables(outstandingReceivables)
                        .build())
                .build();
        summaryCache.set(new CachedSummary(summary, now));
        return summary;
    }

    @Override
    public DashboardStatsResponse getStats() {
        final DashboardSummaryResponse summary = getSummary();
        return DashboardStatsResponse.builder()
                .totalClients(summary.getTotalClients())
                .activeProjects(summary.getActiveProjects())
                .projectsByHealth(summary.getProjectsByHealth())
                .financials(summary.getFinancials())
                .pipeline(PipelineResponse.builder()
                        .totalValue(0D)
                        .weightedValue(0D)
                        .closingThisMonth(0)
                        .build())
                .tasksOverdue(0)
                .milestonesOverdue(0)
                .openIssues(0)
                .criticalRisks(0)
                .build();
    }

    @Override
    public DashboardNotificationsResponse getNotifications() {
        final List<DashboardNotification> notifications = new ArrayList<>();

        final List<Invoice> invoices = invoiceRepository.findAll();
        invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OVERDUE)
                .forEach(invoice -> notifications.add(DashboardNotification.builder()
                        .id("inv-" + invoice.getId())
                        .type("error")
                        .icon("receipt")
                        .title("Facture en retard")
                        .description(invoice.getInvoiceNumber() + " de " + invoice.getClientName())
                        .link("/invoices")
                        .build()));

        final LocalDate today = LocalDate.now();
        invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.SENT && i.getDueDate() != null)
                .forEach(invoice -> {
                    final long days = ChronoUnit.DAYS.between(today, invoice.getDueDate());
                    if (days > 0 && days <= 7) {
                        notifications.add(DashboardNotification.builder()
                                .id("due-" + invoice.getId())
                                .type("info")
                                .icon("receipt")
                                .title("Facture bientot due")
                                .description(invoice.getInvoiceNumber() + " - " + days + " jours")
                                .link("/invoices")
                                .build());
                    }
                });

        final long total = notifications.size();
        final List<DashboardNotification> trimmed = notifications.stream()
                .limit(MAX_NOTIFICATIONS)
                .toList();
        return DashboardNotificationsResponse.builder()
                .notifications(trimmed)
                .total(total)
                .build();
    }

    @Override
    public DashboardProjectHealthResponse getProjectHealth() {
        final List<Project> projects = projectRepository.findAll();
        final SettingsCatalog catalog = settingsCatalogRepository.findById(SettingsCatalogDefaults.CATALOG_ID)
                .orElseGet(SettingsCatalogDefaults::defaultCatalog);
        final ActiveProjectFilters activeFilters = resolveActiveFilters(catalog);

        final long green = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .filter(p -> p.getHealthStatus() == HealthStatus.GREEN)
                .count();
        final long amber = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .filter(p -> p.getHealthStatus() == HealthStatus.AMBER)
                .count();
        final long red = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .filter(p -> p.getHealthStatus() == HealthStatus.RED)
                .count();
        final long total = green + amber + red;

        final List<CriticalProjectResponse> criticalProjects = projects.stream()
                .filter(p -> isActiveProject(p, activeFilters))
                .filter(p -> p.getHealthStatus() == HealthStatus.RED)
                .map(p -> CriticalProjectResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .clientName(p.getClientName())
                        .healthStatus(p.getHealthStatus())
                        .progress(p.getProgress())
                        .openIssues(0)
                        .build())
                .toList();

        return DashboardProjectHealthResponse.builder()
                .distribution(ProjectHealthDistributionResponse.builder()
                        .green(green)
                        .amber(amber)
                        .red(red)
                        .total(total)
                        .build())
                .criticalProjects(criticalProjects)
                .build();
    }

    @Override
    public DashboardFinancialsResponse getFinancials() {
        final List<Invoice> invoices = invoiceRepository.findAll();
        final double totalBilled = invoices.stream()
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();
        final double totalReceived = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();
        final double outstandingReceivables = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.SENT || i.getStatus() == InvoiceStatus.OVERDUE)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();

        final List<Project> projects = projectRepository.findAll();
        final double totalBudget = projects.stream()
                .mapToDouble(p -> p.getClientBudget() != null ? p.getClientBudget() : 0D)
                .sum();
        final double totalCosts = projects.stream()
                .mapToDouble(p -> (p.getVendorCost() != null ? p.getVendorCost() : 0D)
                        + (p.getInternalCost() != null ? p.getInternalCost() : 0D))
                .sum();
        final double netMargin = totalBudget - totalCosts;

        return DashboardFinancialsResponse.builder()
                .revenue(RevenueSummaryResponse.builder()
                        .totalBilled(totalBilled)
                        .totalReceived(totalReceived)
                        .outstandingReceivables(outstandingReceivables)
                        .build())
                .costs(CostsSummaryResponse.builder()
                        .totalBudget(totalBudget)
                        .totalCosts(totalCosts)
                        .build())
                .netMargin(netMargin)
                .currency(CURRENCY_EUR)
                .build();
    }

    @Override
    public UpcomingSchedulesResponse getUpcomingSchedules(final int days, final int limit) {
        final int safeDays = Math.max(1, days);
        final int safeLimit = limit > 0 ? limit : 5;
        final LocalDate today = LocalDate.now();
        final LocalDate end = today.plusDays(safeDays);

        final Query countQuery = new Query();
        countQuery.addCriteria(Criteria.where("scheduleStartDate").gte(today).lte(end));
        final long total = mongoTemplate.count(countQuery, ProjectSchedule.class);

        final Query query = new Query();
        query.addCriteria(Criteria.where("scheduleStartDate").gte(today).lte(end));
        query.with(Sort.by(Sort.Direction.ASC, "scheduleStartDate"));
        query.limit(safeLimit);

        final List<ProjectSchedule> schedules = mongoTemplate.find(query, ProjectSchedule.class);
        final List<UpcomingScheduleItemResponse> items = schedules.stream()
                .map(schedule -> UpcomingScheduleItemResponse.builder()
                        .id(schedule.getId())
                        .projectName(schedule.getProjectName())
                        .clientName(schedule.getClientName())
                        .scheduleStartDate(schedule.getScheduleStartDate())
                        .scheduleEndDate(schedule.getScheduleEndDate())
                        .scheduleColor(schedule.getScheduleColor())
                        .daysUntilStart(schedule.getScheduleStartDate() != null
                                ? ChronoUnit.DAYS.between(today, schedule.getScheduleStartDate())
                                : 0)
                        .build())
                .toList();
        return UpcomingSchedulesResponse.builder()
                .upcomingSchedules(items)
                .total(total)
                .build();
    }

    private record CachedSummary(DashboardSummaryResponse summary, Instant cachedAt) {
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

    private ActiveProjectFilters resolveActiveFilters(final SettingsCatalog catalog) {
        return new ActiveProjectFilters(
                resolveStatusIdByKey(catalog, "closed"),
                resolveStatusIdByKey(catalog, "cancelled"),
                resolveStatusIdByKey(catalog, "delivered")
        );
    }

    private boolean isActiveProject(final Project project, final ActiveProjectFilters filters) {
        if (project == null || filters == null) {
            return false;
        }
        return !matchesStatus(project.getStatusId(), filters.closedId)
                && !matchesStatus(project.getStatusId(), filters.cancelledId)
                && !matchesStatus(project.getStatusId(), filters.deliveredId);
    }

    private record ActiveProjectFilters(String closedId, String cancelledId, String deliveredId) {
    }
}
