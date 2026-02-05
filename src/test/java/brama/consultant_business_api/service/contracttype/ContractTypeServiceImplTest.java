package brama.consultant_business_api.service.contracttype;

import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeUpdateRequest;
import brama.consultant_business_api.domain.contracttype.mapper.ContractTypeMapper;
import brama.consultant_business_api.domain.contracttype.model.ContractTypeConfig;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ContractTypeRepository;
import brama.consultant_business_api.service.contracttype.impl.ContractTypeServiceImpl;
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
class ContractTypeServiceImplTest {
    @Mock
    private ContractTypeRepository repository;

    private ContractTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContractTypeServiceImpl(repository, new ContractTypeMapper());
    }

    @Test
    void listReturnsAll() {
        when(repository.findAll()).thenReturn(List.of(ContractTypeConfig.builder().id("c1").name("Enterprise").key("enterprise").description("Desc").build()));
        var result = service.list();
        assertThat(result).hasSize(1);
    }

    @Test
    void createSavesAndReturns() {
        ContractTypeCreateRequest request = ContractTypeCreateRequest.builder()
                .name("Enterprise")
                .key("enterprise")
                .description("Desc")
                .build();

        when(repository.save(any(ContractTypeConfig.class))).thenAnswer(invocation -> {
            ContractTypeConfig saved = invocation.getArgument(0);
            saved.setId("c1");
            return saved;
        });

        var response = service.create(request);
        assertThat(response.getId()).isEqualTo("c1");
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("c1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("c1", new ContractTypeUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

