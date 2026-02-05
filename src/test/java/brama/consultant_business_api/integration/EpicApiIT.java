package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.epic.dto.request.EpicCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class EpicApiIT extends BaseIntegrationTest {

    @Test
    void epicCrudFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        EpicCreateRequest request = TestDataFactory.epicCreateRequest(projectId, projectRequest.getName());
        ResponseEntity<String> created = post("/api/v1/epics", request);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String epicId = idFrom(created);

        ResponseEntity<String> list = get("/api/v1/epics?projectId=" + projectId);
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/epics/" + epicId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
