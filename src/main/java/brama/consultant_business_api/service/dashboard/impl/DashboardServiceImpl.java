package brama.consultant_business_api.service.dashboard.impl;

import brama.consultant_business_api.domain.dashboard.dto.response.DashboardNotification;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardSummaryResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.FinancialsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.ProjectsByHealthResponse;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.repository.ClientRepository;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
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

    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;
    private final InvoiceRepository invoiceRepository;
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
        final long activeProjects = projects.stream()
                .filter(p -> p.getStatus() != ProjectStatus.CLOSED
                        && p.getStatus() != ProjectStatus.CANCELLED
                        && p.getStatus() != ProjectStatus.DELIVERED)
                .count();
        final long green = projects.stream().filter(p -> p.getHealthStatus() == HealthStatus.GREEN).count();
        final long amber = projects.stream().filter(p -> p.getHealthStatus() == HealthStatus.AMBER).count();
        final long red = projects.stream().filter(p -> p.getHealthStatus() == HealthStatus.RED).count();

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
    public List<DashboardNotification> getNotifications() {
        final List<DashboardNotification> notifications = new ArrayList<>();

        final List<Invoice> invoices = invoiceRepository.findAll();
        invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OVERDUE)
                .forEach(invoice -> notifications.add(DashboardNotification.builder()
                        .id("inv-" + invoice.getId())
                        .type("error")
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
                                .title("Facture bientot due")
                                .description(invoice.getInvoiceNumber() + " - " + days + " jours")
                                .link("/invoices")
                                .build());
                    }
                });

        return notifications;
    }

    private record CachedSummary(DashboardSummaryResponse summary, Instant cachedAt) {
    }
}
