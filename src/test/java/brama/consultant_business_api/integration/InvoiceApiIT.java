package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceExportRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceStatusUpdateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceUpdateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceApiIT extends BaseIntegrationTest {

    @Test
    void invoiceCrudAndExportFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        InvoiceCreateRequest createRequest = TestDataFactory.invoiceCreateRequest(projectId, projectRequest.getName(), clientId, clientRequest.getName());
        ResponseEntity<String> created = post("/api/v1/invoices", createRequest);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String invoiceId = idFrom(created);

        InvoiceUpdateRequest updateRequest = TestDataFactory.invoiceUpdateRequest();
        ResponseEntity<String> updated = patch("/api/v1/invoices/" + invoiceId, updateRequest);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);

        InvoiceStatusUpdateRequest statusRequest = TestDataFactory.invoiceStatusUpdateRequest();
        ResponseEntity<String> statusUpdated = post("/api/v1/invoices/" + invoiceId + "/status", statusRequest);
        assertThat(statusUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(statusUpdated).path("status").asText()).isEqualTo("paid");

        ResponseEntity<String> list = get("/api/v1/invoices?projectId=" + projectId);
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        InvoiceExportRequest exportRequest = new InvoiceExportRequest(List.of(invoiceId));
        ResponseEntity<byte[]> exportResponse = restTemplate.exchange(
                url("/api/v1/invoices/export"),
                HttpMethod.POST,
                new HttpEntity<>(exportRequest, headers),
                byte[].class
        );
        assertThat(exportResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exportResponse.getBody()).isNotNull();

        ResponseEntity<String> deleted = delete("/api/v1/invoices/" + invoiceId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
