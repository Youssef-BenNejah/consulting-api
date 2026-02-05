package brama.consultant_business_api.service.communication;

import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogCreateRequest;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogUpdateRequest;
import brama.consultant_business_api.domain.communication.enums.CommunicationType;
import brama.consultant_business_api.domain.communication.mapper.CommunicationLogMapper;
import brama.consultant_business_api.domain.communication.model.CommunicationLog;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.CommunicationLogRepository;
import brama.consultant_business_api.service.communication.impl.CommunicationLogServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunicationLogServiceImplTest {
    @Mock
    private CommunicationLogRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private CommunicationLogServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CommunicationLogServiceImpl(repository, mongoTemplate, new CommunicationLogMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        CommunicationLog log = CommunicationLog.builder()
                .id("c1")
                .clientId("cl1")
                .clientName("Client")
                .projectId("p1")
                .projectName("Project")
                .date(LocalDate.now())
                .type(CommunicationType.MEETING)
                .summary("Summary")
                .actionItems(List.of("a1"))
                .participants(List.of("p1"))
                .build();

        when(mongoTemplate.count(any(Query.class), eq(CommunicationLog.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(CommunicationLog.class))).thenReturn(List.of(log));

        var result = service.search("cl1", "p1", LocalDate.now().minusDays(1), LocalDate.now(), 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        CommunicationLogCreateRequest request = CommunicationLogCreateRequest.builder()
                .clientId("cl1")
                .clientName("Client")
                .projectId("p1")
                .projectName("Project")
                .date(LocalDate.now())
                .type(CommunicationType.MEETING)
                .summary("Summary")
                .actionItems(List.of("a1"))
                .participants(List.of("p1"))
                .build();

        when(repository.save(any(CommunicationLog.class))).thenAnswer(invocation -> {
            CommunicationLog saved = invocation.getArgument(0);
            saved.setId("c1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("c1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("c1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("c1", new CommunicationLogUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

