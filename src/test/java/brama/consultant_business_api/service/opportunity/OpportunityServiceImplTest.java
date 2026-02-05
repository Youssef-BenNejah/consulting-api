package brama.consultant_business_api.service.opportunity;

import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityCreateRequest;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityUpdateRequest;
import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;
import brama.consultant_business_api.domain.opportunity.mapper.OpportunityMapper;
import brama.consultant_business_api.domain.opportunity.model.Opportunity;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.OpportunityRepository;
import brama.consultant_business_api.service.opportunity.impl.OpportunityServiceImpl;
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
class OpportunityServiceImplTest {
    @Mock
    private OpportunityRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private OpportunityServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new OpportunityServiceImpl(repository, mongoTemplate, new OpportunityMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Opportunity opportunity = Opportunity.builder()
                .id("o1")
                .clientId("c1")
                .clientName("Client")
                .title("Deal")
                .expectedValue(1000D)
                .probability(50D)
                .stage(OpportunityStage.LEAD)
                .expectedCloseDate(LocalDate.now())
                .notes("Notes")
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Opportunity.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Opportunity.class))).thenReturn(List.of(opportunity));

        var result = service.search("c1", OpportunityStage.LEAD, LocalDate.now().minusDays(1), LocalDate.now().plusDays(10), 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        OpportunityCreateRequest request = OpportunityCreateRequest.builder()
                .clientId("c1")
                .clientName("Client")
                .title("Deal")
                .expectedValue(1000D)
                .probability(50D)
                .stage(OpportunityStage.LEAD)
                .expectedCloseDate(LocalDate.now())
                .notes("Notes")
                .build();

        when(repository.save(any(Opportunity.class))).thenAnswer(invocation -> {
            Opportunity saved = invocation.getArgument(0);
            saved.setId("o1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("o1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("o1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("o1", new OpportunityUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

