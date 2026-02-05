package brama.consultant_business_api.service.project;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.enums.ProjectType;

import java.time.LocalDate;

public interface ProjectService {
    PagedResult<ProjectResponse> search(String search,
                                        ProjectStatus status,
                                        String clientId,
                                        ProjectType type,
                                        HealthStatus healthStatus,
                                        LocalDate dateFrom,
                                        LocalDate dateTo,
                                        Integer page,
                                        Integer size,
                                        String sort);

    ProjectResponse create(ProjectCreateRequest request);

    ProjectResponse getById(String id);

    ProjectResponse update(String id, ProjectUpdateRequest request);

    void delete(String id);
}
