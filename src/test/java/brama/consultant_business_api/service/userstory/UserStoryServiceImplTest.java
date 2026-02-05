package brama.consultant_business_api.service.userstory;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryCreateRequest;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryUpdateRequest;
import brama.consultant_business_api.domain.userstory.mapper.UserStoryMapper;
import brama.consultant_business_api.domain.userstory.model.UserStory;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.UserStoryRepository;
import brama.consultant_business_api.service.userstory.impl.UserStoryServiceImpl;
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
class UserStoryServiceImplTest {
    @Mock
    private UserStoryRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private UserStoryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserStoryServiceImpl(repository, mongoTemplate, new UserStoryMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        UserStory story = UserStory.builder()
                .id("u1")
                .epicId("e1")
                .epicTitle("Epic")
                .projectId("p1")
                .projectName("Project")
                .title("Story")
                .description("Desc")
                .acceptanceCriteria(List.of("a"))
                .priority(Priority.MUST)
                .status(StoryStatus.BACKLOG)
                .effort(5)
                .notes("Notes")
                .createdAt(LocalDate.now())
                .build();

        when(mongoTemplate.count(any(Query.class), eq(UserStory.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(UserStory.class))).thenReturn(List.of(story));

        var result = service.search("p1", "e1", StoryStatus.BACKLOG, Priority.MUST, 1, 10);
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        UserStoryCreateRequest request = UserStoryCreateRequest.builder()
                .epicId("e1")
                .epicTitle("Epic")
                .projectId("p1")
                .projectName("Project")
                .title("Story")
                .description("Desc")
                .acceptanceCriteria(List.of("a"))
                .priority(Priority.MUST)
                .status(StoryStatus.BACKLOG)
                .effort(5)
                .notes("Notes")
                .build();

        when(repository.save(any(UserStory.class))).thenAnswer(invocation -> {
            UserStory saved = invocation.getArgument(0);
            saved.setId("u1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("u1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("u1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("u1", new UserStoryUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

