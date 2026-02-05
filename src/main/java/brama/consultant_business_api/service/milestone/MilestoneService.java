package brama.consultant_business_api.service.milestone;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneCreateRequest;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneUpdateRequest;
import brama.consultant_business_api.domain.milestone.dto.response.MilestoneResponse;
import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;

public interface MilestoneService {
    PagedResult<MilestoneResponse> search(String projectId,
                                          MilestoneStatus status,
                                          Integer page,
                                          Integer size);

    MilestoneResponse create(MilestoneCreateRequest request);

    MilestoneResponse update(String id, MilestoneUpdateRequest request);

    void delete(String id);
}
