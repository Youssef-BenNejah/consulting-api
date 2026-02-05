package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.epic.dto.request.EpicCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserStoryApiIT extends BaseIntegrationTest {

    @Test
    void userStoryCrudFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        EpicCreateRequest epicRequest = TestDataFactory.epicCreateRequest(projectId, projectRequest.getName());
        ResponseEntity<String> epicCreated = post("/api/v1/epics", epicRequest);
        String epicId = idFrom(epicCreated);

        UserStoryCreateRequest request = TestDataFactory.userStoryCreateRequest(epicId, epicRequest.getTitle(), projectId, projectRequest.getName());
        ResponseEntity<String> created = post("/api/v1/user-stories", request);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String storyId = idFrom(created);

        ResponseEntity<String> list = get("/api/v1/user-stories?epicId=" + epicId);
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/user-stories/" + storyId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
