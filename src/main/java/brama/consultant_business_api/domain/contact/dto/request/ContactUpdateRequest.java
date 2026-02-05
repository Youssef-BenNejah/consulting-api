package brama.consultant_business_api.domain.contact.dto.request;

import brama.consultant_business_api.domain.contact.enums.ContactStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactUpdateRequest {
    private ContactStatus status;
    @JsonProperty("is_read")
    private Boolean isRead;
}
