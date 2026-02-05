package brama.consultant_business_api.domain.projecttype.mapper;

import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeUpdateRequest;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;
import brama.consultant_business_api.domain.projecttype.model.ProjectTypeConfig;
import org.springframework.stereotype.Component;

@Component
public class ProjectTypeMapper {
    public ProjectTypeConfig toEntity(final ProjectTypeCreateRequest request) {
        if (request == null) {
            return null;
        }
        return ProjectTypeConfig.builder()
                .name(request.getName())
                .key(request.getKey())
                .description(request.getDescription())
                .build();
    }

    public void merge(final ProjectTypeConfig config, final ProjectTypeUpdateRequest request) {
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

    public ProjectTypeResponse toResponse(final ProjectTypeConfig config) {
        if (config == null) {
            return null;
        }
        return ProjectTypeResponse.builder()
                .id(config.getId())
                .name(config.getName())
                .key(config.getKey())
                .description(config.getDescription())
                .build();
    }
}
