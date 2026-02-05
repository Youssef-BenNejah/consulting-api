package brama.consultant_business_api.integration;

import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiDocsIT extends BaseIntegrationTest {

    @Test
    void openApiDocsExposeCorePaths() {
        ResponseEntity<String> response = get("/v3/api-docs");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json(response).path("paths").has("/api/v1/clients")).isTrue();
        assertThat(json(response).path("paths").has("/api/v1/projects")).isTrue();
        assertThat(json(response).path("paths").has("/api/v1/invoices")).isTrue();
    }
}
