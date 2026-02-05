package brama.consultant_business_api.service.issue;

import brama.consultant_business_api.domain.issue.dto.request.IssueCreateRequest;
import brama.consultant_business_api.domain.issue.dto.request.IssueUpdateRequest;
import brama.consultant_business_api.domain.issue.enums.IssueSeverity;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
import brama.consultant_business_api.domain.issue.mapper.IssueMapper;
import brama.consultant_business_api.domain.issue.model.Issue;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.IssueRepository;
import brama.consultant_business_api.service.issue.impl.IssueServiceImpl;
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
class IssueServiceImplTest {
    @Mock
    private IssueRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private IssueServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new IssueServiceImpl(repository, mongoTemplate, new IssueMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Issue issue = Issue.builder()
                .id("i1")
                .title("Issue")
                .description("Desc")
                .projectId("p1")
                .projectName("Project")
                .severity(IssueSeverity.HIGH)
                .owner("Owner")
                .mitigationPlan("Plan")
                .dueDate(LocalDate.now())
                .status(IssueStatus.OPEN)
                .createdAt(LocalDate.now())
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Issue.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Issue.class))).thenReturn(List.of(issue));

        var result = service.search("p1", IssueStatus.OPEN, IssueSeverity.HIGH, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        IssueCreateRequest request = IssueCreateRequest.builder()
                .title("Issue")
                .description("Desc")
                .projectId("p1")
                .projectName("Project")
                .severity(IssueSeverity.HIGH)
                .owner("Owner")
                .mitigationPlan("Plan")
                .dueDate(LocalDate.now())
                .status(IssueStatus.OPEN)
                .build();

        when(repository.save(any(Issue.class))).thenAnswer(invocation -> {
            Issue saved = invocation.getArgument(0);
            saved.setId("i1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("i1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("i1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("i1", new IssueUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

