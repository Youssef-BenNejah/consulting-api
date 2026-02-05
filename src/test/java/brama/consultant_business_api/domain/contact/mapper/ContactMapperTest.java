package brama.consultant_business_api.domain.contact.mapper;

import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.enums.ContactStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContactMapperTest {
    private final ContactMapper mapper = new ContactMapper();

    @Test
    void toEntitySetsDefaults() {
        ContactCreateRequest request = ContactCreateRequest.builder()
                .name("John")
                .email("john@test.com")
                .message("Hello")
                .build();

        var contact = mapper.toEntity(request);
        assertThat(contact.getStatus()).isEqualTo(ContactStatus.NEW);
        assertThat(contact.isRead()).isFalse();
        assertThat(contact.getCreatedAt()).isNotNull();
    }
}

