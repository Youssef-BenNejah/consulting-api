package brama.consultant_business_api.service.dashboard;

import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
import brama.consultant_business_api.domain.issue.model.Issue;
import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
import brama.consultant_business_api.domain.milestone.model.Milestone;
import brama.consultant_business_api.domain.opportunity.model.Opportunity;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.domain.risk.model.Risk;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import brama.consultant_business_api.domain.task.model.Task;
import brama.consultant_business_api.repository.ClientRepository;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.repository.IssueRepository;
import brama.consultant_business_api.repository.MilestoneRepository;
import brama.consultant_business_api.repository.OpportunityRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.repository.RiskRepository;
import brama.consultant_business_api.repository.TaskRepository;
import brama.consultant_business_api.service.dashboard.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {
    @Mock private ClientRepository clientRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private InvoiceRepository invoiceRepository;
    @Mock private OpportunityRepository opportunityRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private MilestoneRepository milestoneRepository;
    @Mock private IssueRepository issueRepository;
    @Mock private RiskRepository riskRepository;

    private DashboardServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DashboardServiceImpl(clientRepository, projectRepository, invoiceRepository, opportunityRepository,
                taskRepository, milestoneRepository, issueRepository, riskRepository);
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
        when(opportunityRepository.findAll()).thenReturn(List.of(
                Opportunity.builder().expectedValue(200D).probability(50D).expectedCloseDate(LocalDate.now()).build()
        ));
        when(taskRepository.findAll()).thenReturn(List.of(
                Task.builder().dueDate(LocalDate.now().minusDays(1)).status(TaskStatus.TODO).build()
        ));
        when(milestoneRepository.findAll()).thenReturn(List.of(
                Milestone.builder().status(MilestoneStatus.OVERDUE).build()
        ));
        when(issueRepository.findAll()).thenReturn(List.of(
                Issue.builder().status(IssueStatus.OPEN).build()
        ));
        when(riskRepository.findAll()).thenReturn(List.of(
                Risk.builder().score(50D).build()
        ));

        var summary = service.getSummary();

        assertThat(summary.getTotalClients()).isEqualTo(2L);
        assertThat(summary.getActiveProjects()).isEqualTo(1L);
        assertThat(summary.getFinancials().getTotalBilled()).isEqualTo(150D);
        assertThat(summary.getCriticalRisks()).isEqualTo(1L);
    }

    @Test
    void notificationsIncludeOverdueAndDueSoon() {
        when(invoiceRepository.findAll()).thenReturn(List.of(
                Invoice.builder().id("i1").status(InvoiceStatus.OVERDUE).invoiceNumber("INV-1").clientName("Client").build(),
                Invoice.builder().id("i2").status(InvoiceStatus.SENT).invoiceNumber("INV-2").clientName("Client").dueDate(LocalDate.now().plusDays(3)).build()
        ));
        when(milestoneRepository.findAll()).thenReturn(List.of(
                Milestone.builder().id("m1").status(MilestoneStatus.OVERDUE).name("M1").projectName("Project").build()
        ));

        var notifications = service.getNotifications();

        assertThat(notifications).isNotEmpty();
        assertThat(notifications.stream().anyMatch(n -> n.getId().startsWith("inv-"))).isTrue();
        assertThat(notifications.stream().anyMatch(n -> n.getId().startsWith("ms-"))).isTrue();
    }
}

