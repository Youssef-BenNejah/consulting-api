package brama.consultant_business_api.service.dashboard;

import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.repository.ClientRepository;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.service.dashboard.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {
    @Mock private ClientRepository clientRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private InvoiceRepository invoiceRepository;

    private DashboardServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DashboardServiceImpl(clientRepository, projectRepository, invoiceRepository);
    }

    @Test
    void summaryComputesMetrics() {
        when(clientRepository.count()).thenReturn(2L);
        when(projectRepository.findAll()).thenReturn(List.of(
                Project.builder().status(ProjectStatus.DELIVERY).healthStatus(HealthStatus.GREEN).build(),
                Project.builder().status(ProjectStatus.CLOSED).healthStatus(HealthStatus.RED).build()
        ));
        when(invoiceRepository.findAll()).thenReturn(List.of(
                Invoice.builder().status(InvoiceStatus.PAID).amount(100D).build(),
                Invoice.builder().status(InvoiceStatus.SENT).amount(50D).build()
        ));

        var summary = service.getSummary();

        assertThat(summary.getTotalClients()).isEqualTo(2L);
        assertThat(summary.getActiveProjects()).isEqualTo(1L);
        assertThat(summary.getFinancials().getTotalBilled()).isEqualTo(150D);
    }

    @Test
    void notificationsIncludeOverdueAndDueSoon() {
        when(invoiceRepository.findAll()).thenReturn(List.of(
                Invoice.builder().id("i1").status(InvoiceStatus.OVERDUE).invoiceNumber("INV-1").clientName("Client").build()
        ));

        var notifications = service.getNotifications();

        assertThat(notifications).isNotEmpty();
        assertThat(notifications.stream().anyMatch(n -> n.getId().startsWith("inv-"))).isTrue();
    }
}

