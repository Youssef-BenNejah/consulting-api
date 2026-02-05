package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTypeApiIT extends BaseIntegrationTest {

    @Test
    void projectTypeCrudFlow() {
        ProjectTypeCreateRequest request = TestDataFactory.projectTypeCreateRequest();
        ResponseEntity<String> created = post("/api/v1/project-types", request);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String typeId = idFrom(created);

        ResponseEntity<String> list = get("/api/v1/project-types");
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(list).size()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/project-types/" + typeId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
