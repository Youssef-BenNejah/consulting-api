package brama.consultant_business_api.integration.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MongoTemplate mongoTemplate;
    @LocalServerPort
    protected int port;

    @BeforeEach
    void cleanDatabase() {
        assertSafeDatabase();
        mongoTemplate.getDb().listCollectionNames()
                .forEach(name -> mongoTemplate.getDb().getCollection(name).drop());
    }

    protected String url(final String path) {
        return "http://localhost:" + port + path;
    }

    protected ResponseEntity<String> get(final String path) {
        return restTemplate.getForEntity(url(path), String.class);
    }

    protected ResponseEntity<String> post(final String path, final Object body) {
        return exchangeJson(path, HttpMethod.POST, body);
    }

    protected ResponseEntity<String> patch(final String path, final Object body) {
        return exchangeJson(path, HttpMethod.PATCH, body);
    }

    protected ResponseEntity<String> delete(final String path) {
        return restTemplate.exchange(url(path), HttpMethod.DELETE, new HttpEntity<>(defaultHeaders()), String.class);
    }

    protected ResponseEntity<String> exchangeJson(final String path, final HttpMethod method, final Object body) {
        return restTemplate.exchange(url(path), method, new HttpEntity<>(body, defaultHeaders()), String.class);
    }

    protected HttpHeaders defaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected JsonNode json(final ResponseEntity<String> response) {
        if (response.getBody() == null) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(response.getBody());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to parse response body", ex);
        }
    }

    protected JsonNode data(final ResponseEntity<String> response) {
        return json(response).path("data");
    }

    protected JsonNode meta(final ResponseEntity<String> response) {
        return json(response).path("meta");
    }

    protected String idFrom(final ResponseEntity<String> response) {
        return data(response).path("id").asText();
    }

    private void assertSafeDatabase() {
        final String name = mongoTemplate.getDb().getName();
        if (name == null || !name.toLowerCase().contains("test")) {
            throw new IllegalStateException("Refusing to run integration tests against non-test database: " + name);
        }
    }
}
