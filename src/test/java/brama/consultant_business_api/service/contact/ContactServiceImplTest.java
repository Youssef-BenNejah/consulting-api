package brama.consultant_business_api.service.contact;

import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactReplyCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactUpdateRequest;
import brama.consultant_business_api.domain.contact.enums.ContactStatus;
import brama.consultant_business_api.domain.contact.mapper.ContactMapper;
import brama.consultant_business_api.domain.contact.mapper.ContactReplyMapper;
import brama.consultant_business_api.domain.contact.model.Contact;
import brama.consultant_business_api.domain.contact.model.ContactReply;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ContactReplyRepository;
import brama.consultant_business_api.repository.ContactRepository;
import brama.consultant_business_api.service.contact.impl.ContactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ContactReplyRepository replyRepository;
    @Mock
    private MongoTemplate mongoTemplate;

    private ContactServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContactServiceImpl(contactRepository, replyRepository, mongoTemplate, new ContactMapper(), new ContactReplyMapper());
    }

    @Test
    void createSetsDefaults() {
        ContactCreateRequest request = ContactCreateRequest.builder()
                .name("John")
                .email("john@test.com")
                .message("Hello")
                .build();

        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(request);

        assertThat(response.getStatus()).isEqualTo(ContactStatus.NEW);
        assertThat(response.isRead()).isFalse();
    }

    @Test
    void searchReturnsPagedResult() {
        Contact contact = Contact.builder().id("c1").name("John").email("john@test.com").message("Hello").status(ContactStatus.NEW).build();

        when(mongoTemplate.count(any(Query.class), eq(Contact.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Contact.class))).thenReturn(List.of(contact));

        var result = service.search("john", ContactStatus.NEW, false, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void updateMissingThrows() {
        when(contactRepository.findById("c1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("c1", new ContactUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void addReplyUpdatesContactStatus() {
        Contact contact = Contact.builder()
                .id("c1")
                .status(ContactStatus.NEW)
                .isRead(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(contactRepository.findById("c1")).thenReturn(Optional.of(contact));
        when(replyRepository.save(any(ContactReply.class))).thenAnswer(invocation -> {
            ContactReply reply = invocation.getArgument(0);
            reply.setId("r1");
            return reply;
        });

        service.addReply("c1", ContactReplyCreateRequest.builder().message("Reply").build());

        assertThat(contact.getStatus()).isEqualTo(ContactStatus.REPLIED);
        assertThat(contact.isRead()).isTrue();
        verify(contactRepository).save(contact);
    }

    @Test
    void deleteMissingThrows() {
        when(contactRepository.existsById("c1")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("c1"))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

