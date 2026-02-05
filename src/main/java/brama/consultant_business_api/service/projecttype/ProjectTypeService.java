package brama.consultant_business_api.service.projecttype;

import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeUpdateRequest;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;

import java.util.List;

public interface ProjectTypeService {
    List<ProjectTypeResponse> list();

    ProjectTypeResponse create(ProjectTypeCreateRequest request);

    ProjectTypeResponse update(String id, ProjectTypeUpdateRequest request);

    void delete(String id);
}
