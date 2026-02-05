package brama.consultant_business_api.domain.contact.mapper;

import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactUpdateRequest;
import brama.consultant_business_api.domain.contact.dto.response.ContactResponse;
import brama.consultant_business_api.domain.contact.enums.ContactStatus;
import brama.consultant_business_api.domain.contact.model.Contact;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ContactMapper {
    public Contact toEntity(final ContactCreateRequest request) {
        if (request == null) {
            return null;
        }
        final Instant now = Instant.now();
        return Contact.builder()
                .name(request.getName())
                .email(request.getEmail())
                .company(request.getCompany())
                .service(request.getService())
                .budget(request.getBudget())
                .message(request.getMessage())
                .status(ContactStatus.NEW)
                .isRead(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void merge(final Contact contact, final ContactUpdateRequest request) {
        if (contact == null || request == null) {
            return;
        }
        if (request.getStatus() != null) {
            contact.setStatus(request.getStatus());
        }
        if (request.getIsRead() != null) {
            contact.setRead(request.getIsRead());
        }
        contact.setUpdatedAt(Instant.now());
    }

    public ContactResponse toResponse(final Contact contact) {
        if (contact == null) {
            return null;
        }
        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .company(contact.getCompany())
                .service(contact.getService())
                .budget(contact.getBudget())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .isRead(contact.isRead())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}
