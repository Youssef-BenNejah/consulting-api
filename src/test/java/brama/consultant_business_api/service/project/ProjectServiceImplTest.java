package brama.consultant_business_api.service.project;

import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.enums.ProjectType;
import brama.consultant_business_api.domain.project.mapper.ProjectMapper;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.service.project.impl.ProjectServiceImpl;
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
class ProjectServiceImplTest {
    @Mock
    private ProjectRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private ProjectServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProjectServiceImpl(repository, mongoTemplate, new ProjectMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Project project = Project.builder()
                .id("p1")
                .projectId("PRJ-1")
                .clientId("c1")
                .clientName("Client")
                .name("Project")
                .description("Desc")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .status(ProjectStatus.DELIVERY)
                .type(ProjectType.FIXED)
                .clientBudget(1000D)
                .vendorCost(200D)
                .internalCost(100D)
                .healthStatus(HealthStatus.GREEN)
                .progress(10)
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Project.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Project.class))).thenReturn(List.of(project));

        var result = service.search("proj", ProjectStatus.DELIVERY, "c1", ProjectType.FIXED,
                HealthStatus.GREEN, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 1, 10, "name,asc");

        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getProjectId()).isEqualTo("PRJ-1");
    }

    @Test
    void createSavesAndReturns() {
        ProjectCreateRequest request = ProjectCreateRequest.builder()
                .projectId("PRJ-1")
                .clientId("c1")
                .clientName("Client")
                .name("Project")
                .description("Desc")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(ProjectStatus.DELIVERY)
                .type(ProjectType.FIXED)
                .clientBudget(1000D)
                .vendorCost(200D)
                .internalCost(100D)
                .healthStatus(HealthStatus.GREEN)
                .progress(10)
                .build();

        when(repository.save(any(Project.class))).thenAnswer(invocation -> {
            Project saved = invocation.getArgument(0);
            saved.setId("p1");
            return saved;
        });

        ProjectResponse response = service.create(request);

        assertThat(response.getId()).isEqualTo("p1");
        assertThat(response.getName()).isEqualTo("Project");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("p1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("p1", new ProjectUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteMissingThrows() {
        when(repository.existsById("p1")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("p1"))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

