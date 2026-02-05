package brama.consultant_business_api.domain.contact.dto.response;

import brama.consultant_business_api.domain.contact.enums.ContactStatus;
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
public class ContactResponse {
    private String id;
    private String name;
    private String email;
    private String company;
    private String service;
    private String budget;
    private String message;
    private ContactStatus status;
    @JsonProperty("is_read")
    private boolean isRead;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
