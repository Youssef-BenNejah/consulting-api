package brama.consultant_business_api.domain.contracttype.mapper;

import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeUpdateRequest;
import brama.consultant_business_api.domain.contracttype.dto.response.ContractTypeResponse;
import brama.consultant_business_api.domain.contracttype.model.ContractTypeConfig;
import org.springframework.stereotype.Component;

@Component
public class ContractTypeMapper {
    public ContractTypeConfig toEntity(final ContractTypeCreateRequest request) {
        if (request == null) {
            return null;
        }
        return ContractTypeConfig.builder()
                .name(request.getName())
                .key(request.getKey())
                .description(request.getDescription())
                .build();
    }

    public void merge(final ContractTypeConfig config, final ContractTypeUpdateRequest request) {
        if (config == null || request == null) {
            return;
        }
        if (request.getName() != null) {
            config.setName(request.getName());
        }
        if (request.getKey() != null) {
            config.setKey(request.getKey());
        }
        if (request.getDescription() != null) {
            config.setDescription(request.getDescription());
        }
    }

    public ContractTypeResponse toResponse(final ContractTypeConfig config) {
        if (config == null) {
            return null;
        }
        return ContractTypeResponse.builder()
                .id(config.getId())
                .name(config.getName())
                .key(config.getKey())
                .description(config.getDescription())
                .build();
    }
}
