package brama.consultant_business_api.service.report;

import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.mapper.InvoiceMapper;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.mapper.ProjectMapper;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.service.report.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private InvoiceRepository invoiceRepository;

    private ReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReportServiceImpl(projectRepository, invoiceRepository, new ProjectMapper(), new InvoiceMapper());
    }

    @Test
    void overviewComputesTotals() {
        when(projectRepository.findAll()).thenReturn(List.of(
                Project.builder().clientBudget(100D).vendorCost(20D).internalCost(10D).build(),
                Project.builder().clientBudget(200D).vendorCost(50D).internalCost(20D).build()
        ));
        when(invoiceRepository.findAll()).thenReturn(List.of(
                Invoice.builder().status(InvoiceStatus.SENT).amount(50D).build(),
                Invoice.builder().status(InvoiceStatus.OVERDUE).amount(25D).build()
        ));

        var overview = service.getOverview();
        assertThat(overview.getTotalRevenue()).isEqualTo(300D);
        assertThat(overview.getTotalReceivables()).isEqualTo(75D);
        assertThat(overview.getOverdueInvoices()).isEqualTo(1L);
    }

    @Test
    void projectsReportReturnsCounts() {
        when(projectRepository.findAll()).thenReturn(List.of(
                Project.builder().status(ProjectStatus.DELIVERY).build(),
                Project.builder().status(ProjectStatus.CLOSED).build()
        ));

        var report = service.getProjectsReport();
        assertThat(report.getStatusCounts().getDelivery()).isEqualTo(1L);
        assertThat(report.getStatusCounts().getClosed()).isEqualTo(1L);
    }

    @Test
    void marginsReportComputesValues() {
        when(projectRepository.findAll()).thenReturn(List.of(
                Project.builder().id("p1").name("P1").clientName("C").clientBudget(100D).vendorCost(20D).internalCost(10D).build()
        ));

        var report = service.getMarginsReport();
        assertThat(report.getTotalMargin()).isEqualTo(70D);
        assertThat(report.getProjects()).hasSize(1);
    }

    @Test
    void invoicesReportReturnsOverdueList() {
        when(invoiceRepository.findAll()).thenReturn(List.of(
                Invoice.builder().status(InvoiceStatus.OVERDUE).amount(10D).build(),
                Invoice.builder().status(InvoiceStatus.PAID).amount(5D).build()
        ));

        var report = service.getInvoicesReport();
        assertThat(report.getOverdueInvoices()).isEqualTo(1L);
        assertThat(report.getOverdue()).hasSize(1);
    }
}

