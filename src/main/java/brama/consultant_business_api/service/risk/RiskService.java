package brama.consultant_business_api.service.risk;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.risk.dto.request.RiskCreateRequest;
import brama.consultant_business_api.domain.risk.dto.request.RiskUpdateRequest;
import brama.consultant_business_api.domain.risk.dto.response.RiskResponse;
import brama.consultant_business_api.domain.risk.enums.RiskStatus;

public interface RiskService {
    PagedResult<RiskResponse> search(String projectId,
                                     RiskStatus status,
                                     Double minScore,
                                     Integer page,
                                     Integer size);

    RiskResponse create(RiskCreateRequest request);

    RiskResponse update(String id, RiskUpdateRequest request);

    void delete(String id);
}
