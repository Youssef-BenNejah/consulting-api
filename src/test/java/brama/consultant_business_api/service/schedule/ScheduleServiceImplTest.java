package brama.consultant_business_api.service.schedule;

import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;
import brama.consultant_business_api.domain.schedule.mapper.ScheduleMapper;
import brama.consultant_business_api.domain.schedule.model.ProjectSchedule;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.repository.ScheduleRepository;
import brama.consultant_business_api.service.schedule.impl.ScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {
    @Mock
    private ScheduleRepository repository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private MongoTemplate mongoTemplate;

    private ScheduleServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ScheduleServiceImpl(repository, projectRepository, mongoTemplate, new ScheduleMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        ProjectSchedule schedule = ProjectSchedule.builder()
                .id("s1")
                .projectId("p1")
                .projectName("Project")
                .clientId("c1")
                .clientName("Client")
                .scheduleStartDate(LocalDate.now())
                .scheduleEndDate(LocalDate.now().plusDays(2))
                .scheduleDurationDays(3)
                .scheduleColor("#000")
                .isScheduled(true)
                .reminderEnabled(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(mongoTemplate.count(any(Query.class), eq(ProjectSchedule.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(ProjectSchedule.class))).thenReturn(List.of(schedule));

        var result = service.search(null, "c1", "p1", null, null, null, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createComputesDuration() {
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .projectId("p1")
                .projectName("Project")
                .clientId("c1")
                .clientName("Client")
                .scheduleStartDate(LocalDate.of(2026, 2, 1))
                .scheduleEndDate(LocalDate.of(2026, 2, 3))
                .scheduleColor("#000")
                .isScheduled(true)
                .reminderEnabled(false)
                .build();

        when(repository.save(any(ProjectSchedule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(request);
        assertThat(response.getScheduleDurationDays()).isEqualTo(3);
    }

    @Test
    void updateInvalidDatesThrow() {
        ProjectSchedule schedule = ProjectSchedule.builder()
                .id("s1")
                .scheduleStartDate(LocalDate.of(2026, 2, 5))
                .scheduleEndDate(LocalDate.of(2026, 2, 6))
                .build();

        when(repository.findById("s1")).thenReturn(Optional.of(schedule));

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
                .scheduleStartDate(LocalDate.of(2026, 2, 7))
                .scheduleEndDate(LocalDate.of(2026, 2, 6))
                .build();

        assertThatThrownBy(() -> service.update("s1", request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void exportCsvBuildsContent() {
        ProjectSchedule schedule = ProjectSchedule.builder()
                .projectId("p1")
                .projectName("Project")
                .clientId("c1")
                .clientName("Client")
                .scheduleStartDate(LocalDate.of(2026, 2, 1))
                .scheduleEndDate(LocalDate.of(2026, 2, 2))
                .scheduleDurationDays(2)
                .scheduleColor("#000")
                .isScheduled(true)
                .reminderEnabled(false)
                .build();

        when(mongoTemplate.find(any(Query.class), eq(ProjectSchedule.class))).thenReturn(List.of(schedule));
        when(projectRepository.findAll()).thenReturn(List.of(Project.builder().id("p1").status(ProjectStatus.DELIVERY).build()));

        byte[] csv = service.exportCsv(null, null, null, ProjectStatus.DELIVERY, null, null);
        String content = new String(csv);
        assertThat(content).contains("project_id,project_name");
        assertThat(content).contains("p1");
    }

    @Test
    void deleteMissingThrows() {
        when(repository.existsById("s1")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("s1"))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

