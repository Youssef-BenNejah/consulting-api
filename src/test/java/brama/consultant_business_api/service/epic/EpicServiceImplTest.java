package brama.consultant_business_api.service.epic;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.epic.dto.request.EpicCreateRequest;
import brama.consultant_business_api.domain.epic.dto.request.EpicUpdateRequest;
import brama.consultant_business_api.domain.epic.mapper.EpicMapper;
import brama.consultant_business_api.domain.epic.model.Epic;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.EpicRepository;
import brama.consultant_business_api.service.epic.impl.EpicServiceImpl;
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
class EpicServiceImplTest {
    @Mock
    private EpicRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private EpicServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EpicServiceImpl(repository, mongoTemplate, new EpicMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Epic epic = Epic.builder()
                .id("e1")
                .projectId("p1")
                .projectName("Project")
                .title("Epic")
                .description("Desc")
                .priority(Priority.MUST)
                .status(StoryStatus.BACKLOG)
                .progress(10)
                .storyCount(2)
                .createdAt(LocalDate.now())
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Epic.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Epic.class))).thenReturn(List.of(epic));

        var result = service.search("p1", StoryStatus.BACKLOG, Priority.MUST, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        EpicCreateRequest request = EpicCreateRequest.builder()
                .projectId("p1")
                .projectName("Project")
                .title("Epic")
                .description("Desc")
                .priority(Priority.MUST)
                .status(StoryStatus.BACKLOG)
                .progress(10)
                .storyCount(2)
                .build();

        when(repository.save(any(Epic.class))).thenAnswer(invocation -> {
            Epic saved = invocation.getArgument(0);
            saved.setId("e1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("e1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("e1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("e1", new EpicUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

