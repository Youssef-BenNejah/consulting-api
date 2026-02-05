package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.client.dto.request.ClientUpdateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ClientApiIT extends BaseIntegrationTest {

    @Test
    void clientCrudFlow() {
        final ClientCreateRequest createRequest = TestDataFactory.clientCreateRequest();

        ResponseEntity<String> created = post("/api/v1/clients", createRequest);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final String clientId = idFrom(created);

        ResponseEntity<String> fetched = get("/api/v1/clients/" + clientId);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(fetched).path("name").asText()).isEqualTo(createRequest.getName());

        ClientUpdateRequest updateRequest = TestDataFactory.clientUpdateRequest();
        ResponseEntity<String> updated = patch("/api/v1/clients/" + clientId, updateRequest);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(updated).path("industry").asText()).isEqualTo("FinTech");

        ResponseEntity<String> list = get("/api/v1/clients?search=Acme");
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/clients/" + clientId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> missing = get("/api/v1/clients/" + clientId);
        assertThat(missing.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void clientValidationRejectsInvalidEmail() {
        ClientCreateRequest createRequest = TestDataFactory.clientCreateRequest();
        createRequest.setEmail("invalid-email");

        ResponseEntity<String> response = post("/api/v1/clients", createRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
