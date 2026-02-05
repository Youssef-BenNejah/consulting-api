package brama.consultant_business_api.service.task;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.dto.request.TaskCreateRequest;
import brama.consultant_business_api.domain.task.dto.request.TaskUpdateRequest;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import brama.consultant_business_api.domain.task.mapper.TaskMapper;
import brama.consultant_business_api.domain.task.model.Task;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.TaskRepository;
import brama.consultant_business_api.service.task.impl.TaskServiceImpl;
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
class TaskServiceImplTest {
    @Mock
    private TaskRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private TaskServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TaskServiceImpl(repository, mongoTemplate, new TaskMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Task task = Task.builder()
                .id("t1")
                .title("Task")
                .description("Desc")
                .projectId("p1")
                .projectName("Project")
                .owner("Owner")
                .ownerType(OwnerType.INTERNAL)
                .dueDate(LocalDate.now())
                .status(TaskStatus.TODO)
                .priority(Priority.MUST)
                .estimatedHours(2D)
                .actualHours(0D)
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Task.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Task.class))).thenReturn(List.of(task));

        var result = service.search("p1", TaskStatus.TODO, Priority.MUST, OwnerType.INTERNAL, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        TaskCreateRequest request = TaskCreateRequest.builder()
                .title("Task")
                .description("Desc")
                .projectId("p1")
                .projectName("Project")
                .owner("Owner")
                .ownerType(OwnerType.INTERNAL)
                .dueDate(LocalDate.now())
                .status(TaskStatus.TODO)
                .priority(Priority.MUST)
                .estimatedHours(2D)
                .actualHours(0D)
                .build();

        when(repository.save(any(Task.class))).thenAnswer(invocation -> {
            Task saved = invocation.getArgument(0);
            saved.setId("t1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("t1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("t1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("t1", new TaskUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

