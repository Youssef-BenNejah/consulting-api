package brama.consultant_business_api.service.client;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.client.dto.request.ClientUpdateRequest;
import brama.consultant_business_api.domain.client.dto.response.ClientResponse;
import brama.consultant_business_api.domain.client.mapper.ClientMapper;
import brama.consultant_business_api.domain.client.model.Client;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ClientRepository;
import brama.consultant_business_api.service.client.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    private ClientRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientServiceImpl(repository, mongoTemplate, new ClientMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Client client = Client.builder()
                .id("c1")
                .name("Acme")
                .industry("Tech")
                .country("FR")
                .primaryContact("John")
                .email("john@acme.fr")
                .phone("123")
                .contractType("Enterprise")
                .notes("note")
                .tags(List.of("t1"))
                .createdAt(LocalDate.now())
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Client.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Client.class))).thenReturn(List.of(client));

        var result = service.search("acme", "Tech", "FR", List.of("t1"), 1, 10, "name,asc");

        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("Acme");
    }

    @Test
    void createSavesAndReturns() {
        ClientCreateRequest request = ClientCreateRequest.builder()
                .name("Acme")
                .industry("Tech")
                .country("FR")
                .primaryContact("John")
                .email("john@acme.fr")
                .phone("123")
                .contractType("Enterprise")
                .notes("note")
                .tags(List.of("t1"))
                .build();

        when(repository.save(any(Client.class))).thenAnswer(invocation -> {
            Client saved = invocation.getArgument(0);
            saved.setId("c1");
            return saved;
        });

        ClientResponse response = service.create(request);

        assertThat(response.getId()).isEqualTo("c1");
        assertThat(response.getName()).isEqualTo("Acme");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("c1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("c1", new ClientUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteMissingThrows() {
        when(repository.existsById("c1")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("c1"))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

