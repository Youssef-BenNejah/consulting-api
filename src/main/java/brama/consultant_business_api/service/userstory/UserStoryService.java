package brama.consultant_business_api.service.userstory;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryCreateRequest;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryUpdateRequest;
import brama.consultant_business_api.domain.userstory.dto.response.UserStoryResponse;

public interface UserStoryService {
    PagedResult<UserStoryResponse> search(String projectId,
                                          String epicId,
                                          StoryStatus status,
                                          Priority priority,
                                          Integer page,
                                          Integer size);

    UserStoryResponse create(UserStoryCreateRequest request);

    UserStoryResponse update(String id, UserStoryUpdateRequest request);

    void delete(String id);
}
