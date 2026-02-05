package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import brama.consultant_business_api.support.TestUploadFile;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = "cloudinary.enabled=true")
class DocumentCloudinaryApiIT extends BaseIntegrationTest {

    @Test
    void uploadsAndDownloadsWithCloudinary() {
        TestUploadFile.assumeExists();

        ClientCreateRequest clientRequest = TestDataFactory.clientCreateRequest();
        String clientId = idFrom(post("/api/v1/clients", clientRequest));

        ProjectCreateRequest projectRequest = TestDataFactory.projectCreateRequest(clientId, clientRequest.getName());
        String projectId = idFrom(post("/api/v1/projects", projectRequest));

        FileSystemResource fileResource = TestUploadFile.fileSystemResource();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("name", "1600w-a1RYzvS1EFo.webp");
        body.add("category", "contract");
        body.add("clientId", clientId);
        body.add("clientName", clientRequest.getName());
        body.add("projectId", projectId);
        body.add("projectName", projectRequest.getName());
        body.add("uploadedBy", "System");
        body.add("uploadedAt", LocalDate.now().toString());
        body.add("size", "160 KB");
        body.add("fileType", "WEBP");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<String> uploaded = restTemplate.postForEntity(
                url("/api/v1/documents"),
                new HttpEntity<>(body, headers),
                String.class
        );

        assertThat(uploaded.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String uploadedId = idFrom(uploaded);
        assertThat(data(uploaded).path("storageProvider").asText()).isEqualTo("cloudinary");
        assertThat(data(uploaded).path("fileUrl").asText()).isNotBlank();

        ResponseEntity<byte[]> download = restTemplate.getForEntity(
                url("/api/v1/documents/" + uploadedId + "/download"),
                byte[].class
        );
        assertThat(download.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(download.getBody()).isNotNull();
        assertThat(download.getBody().length).isGreaterThan(0);

        ResponseEntity<String> deleted = delete("/api/v1/documents/" + uploadedId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
