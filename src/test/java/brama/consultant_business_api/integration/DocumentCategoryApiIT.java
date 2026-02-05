package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentCategoryApiIT extends BaseIntegrationTest {

    @Test
    void documentCategoryCrudFlow() {
        DocumentCategoryCreateRequest request = TestDataFactory.documentCategoryCreateRequest();
        ResponseEntity<String> created = post("/api/v1/document-categories", request);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String categoryId = idFrom(created);

        ResponseEntity<String> list = get("/api/v1/document-categories");
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(list).size()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/document-categories/" + categoryId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
