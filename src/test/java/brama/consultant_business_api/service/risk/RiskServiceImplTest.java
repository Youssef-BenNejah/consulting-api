package brama.consultant_business_api.service.risk;

import brama.consultant_business_api.domain.risk.dto.request.RiskCreateRequest;
import brama.consultant_business_api.domain.risk.dto.request.RiskUpdateRequest;
import brama.consultant_business_api.domain.risk.enums.RiskStatus;
import brama.consultant_business_api.domain.risk.mapper.RiskMapper;
import brama.consultant_business_api.domain.risk.model.Risk;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.RiskRepository;
import brama.consultant_business_api.service.risk.impl.RiskServiceImpl;
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
class RiskServiceImplTest {
    @Mock
    private RiskRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private RiskServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RiskServiceImpl(repository, mongoTemplate, new RiskMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Risk risk = Risk.builder()
                .id("r1")
                .title("Risk")
                .description("Desc")
                .projectId("p1")
                .projectName("Project")
                .probability(50D)
                .impact(80D)
                .score(40D)
                .owner("Owner")
                .mitigationPlan("Plan")
                .dueDate(LocalDate.now())
                .status(RiskStatus.IDENTIFIED)
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Risk.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Risk.class))).thenReturn(List.of(risk));

        var result = service.search("p1", RiskStatus.IDENTIFIED, 10D, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        RiskCreateRequest request = RiskCreateRequest.builder()
                .title("Risk")
                .description("Desc")
                .projectId("p1")
                .projectName("Project")
                .probability(50D)
                .impact(80D)
                .owner("Owner")
                .mitigationPlan("Plan")
                .dueDate(LocalDate.now())
                .status(RiskStatus.IDENTIFIED)
                .build();

        when(repository.save(any(Risk.class))).thenAnswer(invocation -> {
            Risk saved = invocation.getArgument(0);
            saved.setId("r1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("r1");
        assertThat(response.getScore()).isEqualTo(40D);
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("r1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("r1", new RiskUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

