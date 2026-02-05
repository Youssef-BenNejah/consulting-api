package brama.consultant_business_api.service.opportunity;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityCreateRequest;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityUpdateRequest;
import brama.consultant_business_api.domain.opportunity.dto.response.OpportunityResponse;
import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;

import java.time.LocalDate;

public interface OpportunityService {
    PagedResult<OpportunityResponse> search(String clientId,
                                            OpportunityStage stage,
                                            LocalDate expectedCloseFrom,
                                            LocalDate expectedCloseTo,
                                            Integer page,
                                            Integer size);

    OpportunityResponse create(OpportunityCreateRequest request);

    OpportunityResponse update(String id, OpportunityUpdateRequest request);

    void delete(String id);
}
