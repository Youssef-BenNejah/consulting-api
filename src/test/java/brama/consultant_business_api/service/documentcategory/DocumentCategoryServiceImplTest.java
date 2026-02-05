package brama.consultant_business_api.service.documentcategory;

import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryUpdateRequest;
import brama.consultant_business_api.domain.documentcategory.mapper.DocumentCategoryMapper;
import brama.consultant_business_api.domain.documentcategory.model.DocumentCategory;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.DocumentCategoryRepository;
import brama.consultant_business_api.service.documentcategory.impl.DocumentCategoryServiceImpl;
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
class DocumentCategoryServiceImplTest {
    @Mock
    private DocumentCategoryRepository repository;

    private DocumentCategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DocumentCategoryServiceImpl(repository, new DocumentCategoryMapper());
    }

    @Test
    void listReturnsAll() {
        when(repository.findAll()).thenReturn(List.of(DocumentCategory.builder().id("d1").name("Contract").key("contract").color("red").build()));
        var result = service.list();
        assertThat(result).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        DocumentCategoryCreateRequest request = DocumentCategoryCreateRequest.builder()
                .name("Contract")
                .key("contract")
                .color("red")
                .build();

        when(repository.save(any(DocumentCategory.class))).thenAnswer(invocation -> {
            DocumentCategory saved = invocation.getArgument(0);
            saved.setId("d1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("d1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("d1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("d1", new DocumentCategoryUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

