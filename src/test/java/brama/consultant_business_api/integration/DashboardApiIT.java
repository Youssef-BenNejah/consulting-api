package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.issue.dto.request.IssueCreateRequest;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneCreateRequest;
import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.risk.dto.request.RiskCreateRequest;
import brama.consultant_business_api.domain.task.dto.request.TaskCreateRequest;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

class DashboardApiIT extends BaseIntegrationTest {

    @Test
    void dashboardSummaryAndNotificationsReflectData() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        projectRequest.setStatus(ProjectStatus.DELIVERY);
        projectRequest.setHealthStatus(HealthStatus.RED);
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        InvoiceCreateRequest invoiceRequest = TestDataFactory.invoiceCreateRequest(projectId, projectRequest.getName(), clientId, clientRequest.getName());
        invoiceRequest.setStatus(InvoiceStatus.OVERDUE);
        invoiceRequest.setDate(LocalDate.now().minusDays(10));
        invoiceRequest.setDueDate(LocalDate.now().minusDays(2));
        post("/api/v1/invoices", invoiceRequest);

        OpportunityCreateRequest opportunityRequest = TestDataFactory.opportunityCreateRequest(clientId, clientRequest.getName());
        opportunityRequest.setExpectedCloseDate(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
        post("/api/v1/opportunities", opportunityRequest);

        TaskCreateRequest taskRequest = TestDataFactory.taskCreateRequest(projectId, projectRequest.getName());
        taskRequest.setDueDate(LocalDate.now().minusDays(1));
        taskRequest.setStatus(TaskStatus.TODO);
        post("/api/v1/tasks", taskRequest);

        MilestoneCreateRequest milestoneRequest = TestDataFactory.milestoneCreateRequest(projectId, projectRequest.getName());
        milestoneRequest.setStatus(MilestoneStatus.OVERDUE);
        post("/api/v1/milestones", milestoneRequest);

        IssueCreateRequest issueRequest = TestDataFactory.issueCreateRequest(projectId, projectRequest.getName());
        issueRequest.setStatus(IssueStatus.OPEN);
        post("/api/v1/issues", issueRequest);

        RiskCreateRequest riskRequest = TestDataFactory.riskCreateRequest(projectId, projectRequest.getName());
        riskRequest.setProbability(80D);
        riskRequest.setImpact(80D);
        post("/api/v1/risks", riskRequest);

        ResponseEntity<String> summary = get("/api/v1/dashboard/summary");
        assertThat(summary.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(summary).path("totalClients").asInt()).isEqualTo(1);
        assertThat(data(summary).path("activeProjects").asInt()).isEqualTo(1);
        assertThat(data(summary).path("projectsByHealth").path("red").asInt()).isEqualTo(1);

        ResponseEntity<String> notifications = get("/api/v1/dashboard/notifications");
        assertThat(notifications.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(notifications).size()).isGreaterThanOrEqualTo(1);
    }
}
