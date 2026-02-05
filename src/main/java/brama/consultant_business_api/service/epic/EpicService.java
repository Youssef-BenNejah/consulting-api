package brama.consultant_business_api.service.epic;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.epic.dto.request.EpicCreateRequest;
import brama.consultant_business_api.domain.epic.dto.request.EpicUpdateRequest;
import brama.consultant_business_api.domain.epic.dto.response.EpicResponse;

public interface EpicService {
    PagedResult<EpicResponse> search(String projectId,
                                     StoryStatus status,
                                     Priority priority,
                                     Integer page,
                                     Integer size);

    EpicResponse create(EpicCreateRequest request);

    EpicResponse update(String id, EpicUpdateRequest request);

    void delete(String id);
}
