package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectApiIT extends BaseIntegrationTest {

    @Test
    void projectCrudFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        ResponseEntity<String> clientCreated = post("/api/v1/clients", clientRequest);
        String clientId = idFrom(clientCreated);

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        ResponseEntity<String> created = post("/api/v1/projects", projectRequest);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String projectId = idFrom(created);

        ResponseEntity<String> fetched = get("/api/v1/projects/" + projectId);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(fetched).path("projectId").asText()).isEqualTo(projectRequest.getProjectId());

        ProjectUpdateRequest updateRequest = TestDataFactory.projectUpdateRequest();
        ResponseEntity<String> updated = patch("/api/v1/projects/" + projectId, updateRequest);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(updated).path("progress").asInt()).isEqualTo(40);

        ResponseEntity<String> list = get("/api/v1/projects?status=review");
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/projects/" + projectId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
