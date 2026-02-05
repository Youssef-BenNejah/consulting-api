package brama.consultant_business_api.domain.contact.mapper;

import brama.consultant_business_api.domain.contact.dto.request.ContactReplyCreateRequest;
import brama.consultant_business_api.domain.contact.dto.response.ContactReplyResponse;
import brama.consultant_business_api.domain.contact.model.ContactReply;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ContactReplyMapper {
    public ContactReply toEntity(final String contactId, final ContactReplyCreateRequest request) {
        if (request == null) {
            return null;
        }
        return ContactReply.builder()
                .contactId(contactId)
                .message(request.getMessage())
                .sentAt(Instant.now())
                .build();
    }

    public ContactReplyResponse toResponse(final ContactReply reply) {
        if (reply == null) {
            return null;
        }
        return ContactReplyResponse.builder()
                .id(reply.getId())
                .contactId(reply.getContactId())
                .message(reply.getMessage())
                .sentAt(reply.getSentAt())
                .build();
    }
}
