package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.document.dto.request.DocumentCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentApiIT extends BaseIntegrationTest {

    @Test
    void documentMetadataAndUploadFlow() {
        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        DocumentCreateRequest metadataRequest = TestDataFactory.documentCreateRequest(projectId, projectRequest.getName(), clientId, clientRequest.getName());
        ResponseEntity<String> createdMetadata = post("/api/v1/documents", metadataRequest);
        assertThat(createdMetadata.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String metadataId = idFrom(createdMetadata);

        ResponseEntity<String> fetched = get("/api/v1/documents/" + metadataId);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(fetched).path("name").asText()).isEqualTo(metadataRequest.getName());

        ByteArrayResource fileResource = new ByteArrayResource("sample document".getBytes(StandardCharsets.UTF_8)) {
            @Override
            public String getFilename() {
                return "sample.txt";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("name", "sample.txt");
        body.add("category", "contract");
        body.add("clientId", clientId);
        body.add("clientName", clientRequest.getName());
        body.add("projectId", projectId);
        body.add("projectName", projectRequest.getName());
        body.add("uploadedBy", "System");
        body.add("uploadedAt", LocalDate.now().toString());
        body.add("size", "8KB");
        body.add("fileType", "TXT");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        ResponseEntity<String> uploaded = restTemplate.postForEntity(
                url("/api/v1/documents"),
                new HttpEntity<>(body, headers),
                String.class
        );
        assertThat(uploaded.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String uploadedId = idFrom(uploaded);

        ResponseEntity<byte[]> download = restTemplate.getForEntity(url("/api/v1/documents/" + uploadedId + "/download"), byte[].class);
        assertThat(download.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(download.getBody()).isNotNull();

        ResponseEntity<String> list = get("/api/v1/documents?projectId=" + projectId);
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meta(list).path("total").asInt()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/documents/" + uploadedId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
