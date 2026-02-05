package brama.consultant_business_api.domain.project.mapper;

import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
import brama.consultant_business_api.domain.project.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public Project toEntity(final ProjectCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Project.builder()
                .projectId(request.getProjectId())
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .type(request.getType())
                .clientBudget(request.getClientBudget())
                .vendorCost(request.getVendorCost())
                .internalCost(request.getInternalCost())
                .healthStatus(request.getHealthStatus())
                .progress(request.getProgress())
                .build();
    }

    public void merge(final Project project, final ProjectUpdateRequest request) {
        if (project == null || request == null) {
            return;
        }
        if (request.getProjectId() != null) {
            project.setProjectId(request.getProjectId());
        }
        if (request.getClientId() != null) {
            project.setClientId(request.getClientId());
        }
        if (request.getClientName() != null) {
            project.setClientName(request.getClientName());
        }
        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            project.setEndDate(request.getEndDate());
        }
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        if (request.getType() != null) {
            project.setType(request.getType());
        }
        if (request.getClientBudget() != null) {
            project.setClientBudget(request.getClientBudget());
        }
        if (request.getVendorCost() != null) {
            project.setVendorCost(request.getVendorCost());
        }
        if (request.getInternalCost() != null) {
            project.setInternalCost(request.getInternalCost());
        }
        if (request.getHealthStatus() != null) {
            project.setHealthStatus(request.getHealthStatus());
        }
        if (request.getProgress() != null) {
            project.setProgress(request.getProgress());
        }
    }

    public ProjectResponse toResponse(final Project project) {
        if (project == null) {
            return null;
        }
        return ProjectResponse.builder()
                .id(project.getId())
                .projectId(project.getProjectId())
                .clientId(project.getClientId())
                .clientName(project.getClientName())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .type(project.getType())
                .clientBudget(project.getClientBudget())
                .vendorCost(project.getVendorCost())
                .internalCost(project.getInternalCost())
                .healthStatus(project.getHealthStatus())
                .progress(project.getProgress())
                .createdAt(project.getCreatedDate())
                .updatedAt(project.getLastModifiedDate())
                .build();
    }
}
