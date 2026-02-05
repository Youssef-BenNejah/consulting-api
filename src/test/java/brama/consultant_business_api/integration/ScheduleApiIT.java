package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleApiIT extends BaseIntegrationTest {

    @Test
    void scheduleCrudAndExportFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        ScheduleCreateRequest scheduleRequest = TestDataFactory.scheduleCreateRequest(projectId, projectRequest.getName(), clientId, clientRequest.getName());
        ResponseEntity<String> created = post("/api/v1/schedules", scheduleRequest);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String scheduleId = idFrom(created);

        ScheduleUpdateRequest updateRequest = TestDataFactory.scheduleUpdateRequest();
        ResponseEntity<String> updated = patch("/api/v1/schedules/" + scheduleId, updateRequest);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> list = get("/api/v1/schedules?projectId=" + projectId);
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<byte[]> export = restTemplate.getForEntity(url("/api/v1/schedules/export?format=csv"), byte[].class);
        assertThat(export.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(export.getBody()).isNotNull();

        ResponseEntity<String> deleted = delete("/api/v1/schedules/" + scheduleId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
