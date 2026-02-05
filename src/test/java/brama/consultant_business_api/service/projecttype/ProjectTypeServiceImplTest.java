package brama.consultant_business_api.service.projecttype;

import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeUpdateRequest;
import brama.consultant_business_api.domain.projecttype.mapper.ProjectTypeMapper;
import brama.consultant_business_api.domain.projecttype.model.ProjectTypeConfig;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ProjectTypeRepository;
import brama.consultant_business_api.service.projecttype.impl.ProjectTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectTypeServiceImplTest {
    @Mock
    private ProjectTypeRepository repository;

    private ProjectTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProjectTypeServiceImpl(repository, new ProjectTypeMapper());
    }

    @Test
    void listReturnsAll() {
        when(repository.findAll()).thenReturn(List.of(ProjectTypeConfig.builder().id("p1").name("Fixed").key("fixed").description("Desc").build()));
        var result = service.list();
        assertThat(result).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        ProjectTypeCreateRequest request = ProjectTypeCreateRequest.builder()
                .name("Fixed")
                .key("fixed")
                .description("Desc")
                .build();

        when(repository.save(any(ProjectTypeConfig.class))).thenAnswer(invocation -> {
            ProjectTypeConfig saved = invocation.getArgument(0);
            saved.setId("p1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("p1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("p1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("p1", new ProjectTypeUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

