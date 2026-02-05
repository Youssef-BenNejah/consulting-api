package brama.consultant_business_api.service.milestone;

import brama.consultant_business_api.domain.milestone.dto.request.MilestoneCreateRequest;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneUpdateRequest;
import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
import brama.consultant_business_api.domain.milestone.mapper.MilestoneMapper;
import brama.consultant_business_api.domain.milestone.model.Milestone;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.MilestoneRepository;
import brama.consultant_business_api.service.milestone.impl.MilestoneServiceImpl;
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
class MilestoneServiceImplTest {
    @Mock
    private MilestoneRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private MilestoneServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MilestoneServiceImpl(repository, mongoTemplate, new MilestoneMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Milestone milestone = Milestone.builder()
                .id("m1")
                .name("M1")
                .projectId("p1")
                .projectName("Project")
                .dueDate(LocalDate.now())
                .deliverable("Del")
                .acceptanceCriteria("Crit")
                .status(MilestoneStatus.PENDING)
                .signOffBy("User")
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Milestone.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Milestone.class))).thenReturn(List.of(milestone));

        var result = service.search("p1", MilestoneStatus.PENDING, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        MilestoneCreateRequest request = MilestoneCreateRequest.builder()
                .name("M1")
                .projectId("p1")
                .projectName("Project")
                .dueDate(LocalDate.now())
                .deliverable("Del")
                .acceptanceCriteria("Crit")
                .status(MilestoneStatus.PENDING)
                .signOffBy("User")
                .build();

        when(repository.save(any(Milestone.class))).thenAnswer(invocation -> {
            Milestone saved = invocation.getArgument(0);
            saved.setId("m1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("m1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("m1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("m1", new MilestoneUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

