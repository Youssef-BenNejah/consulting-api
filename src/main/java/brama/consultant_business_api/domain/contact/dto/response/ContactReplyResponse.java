package brama.consultant_business_api.domain.contact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactReplyResponse {
    private String id;
    @JsonProperty("contact_id")
    private String contactId;
    private String message;
    @JsonProperty("sent_at")
    private Instant sentAt;
}
