package brama.consultant_business_api.service.task;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.dto.request.TaskCreateRequest;
import brama.consultant_business_api.domain.task.dto.request.TaskUpdateRequest;
import brama.consultant_business_api.domain.task.dto.response.TaskResponse;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;

public interface TaskService {
    PagedResult<TaskResponse> search(String projectId,
                                     TaskStatus status,
                                     Priority priority,
                                     OwnerType ownerType,
                                     Integer page,
                                     Integer size);

    TaskResponse create(TaskCreateRequest request);

    TaskResponse update(String id, TaskUpdateRequest request);

    void delete(String id);
}
