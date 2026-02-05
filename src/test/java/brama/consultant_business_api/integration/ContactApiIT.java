package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactReplyCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactUpdateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ContactApiIT extends BaseIntegrationTest {

    @Test
    void contactAndRepliesFlow() {
        ContactCreateRequest createRequest = TestDataFactory.contactCreateRequest();
        ResponseEntity<String> created = post("/api/v1/contacts", createRequest);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String contactId = idFrom(created);

        ContactUpdateRequest updateRequest = TestDataFactory.contactUpdateRequest();
        ResponseEntity<String> updated = patch("/api/v1/contacts/" + contactId, updateRequest);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(updated).path("is_read").asBoolean()).isTrue();

        ContactReplyCreateRequest replyRequest = TestDataFactory.contactReplyCreateRequest();
        ResponseEntity<String> replyCreated = post("/api/v1/contacts/" + contactId + "/replies", replyRequest);
        assertThat(replyCreated.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> replies = get("/api/v1/contacts/" + contactId + "/replies");
        assertThat(replies.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(replies).isArray()).isTrue();

        ResponseEntity<String> list = get("/api/v1/contacts?is_read=true");
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> deleted = delete("/api/v1/contacts/" + contactId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
