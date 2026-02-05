package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.issue.dto.request.IssueCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class IssueApiIT extends BaseIntegrationTest {

    @Test
    void issueCrudFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        ResponseEntity<String> projectCreated = post("/api/v1/projects", projectRequest);
        String projectId = idFrom(projectCreated);

        IssueCreateRequest issueRequest = TestDataFactory.issueCreateRequest(projectId, projectRequest.getName());
        ResponseEntity<String> created = post("/api/v1/issues", issueRequest);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String issueId = idFrom(created);

        ResponseEntity<String> list = get("/api/v1/issues?projectId=" + projectId);
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/issues/" + issueId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
