package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class OpportunityApiIT extends BaseIntegrationTest {

    @Test
    void opportunityCrudFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        OpportunityCreateRequest request = TestDataFactory.opportunityCreateRequest(clientId, clientRequest.getName());
        ResponseEntity<String> created = post("/api/v1/opportunities", request);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String oppId = idFrom(created);

        ResponseEntity<String> list = get("/api/v1/opportunities?clientId=" + clientId);
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/opportunities/" + oppId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
