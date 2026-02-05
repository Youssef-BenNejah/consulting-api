package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ReportsApiIT extends BaseIntegrationTest {

    @Test
    void reportsEndpointsReturnPayloads() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        InvoiceCreateRequest invoiceRequest = TestDataFactory.invoiceCreateRequest(projectId, projectRequest.getName(), clientId, clientRequest.getName());
        post("/api/v1/invoices", invoiceRequest);

        ResponseEntity<String> overview = get("/api/v1/reports/overview");
        assertThat(overview.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(overview).path("totalRevenue").isNumber()).isTrue();

        ResponseEntity<String> projects = get("/api/v1/reports/projects");
        assertThat(projects.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(projects).path("projects").isArray()).isTrue();

        ResponseEntity<String> margins = get("/api/v1/reports/margins");
        assertThat(margins.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(margins).path("projects").isArray()).isTrue();

        ResponseEntity<String> invoices = get("/api/v1/reports/invoices");
        assertThat(invoices.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(invoices).path("totalInvoices").asInt()).isGreaterThanOrEqualTo(1);
    }
}
