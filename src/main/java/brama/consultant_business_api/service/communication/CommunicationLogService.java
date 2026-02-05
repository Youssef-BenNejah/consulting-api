package brama.consultant_business_api.service.communication;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogCreateRequest;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogUpdateRequest;
import brama.consultant_business_api.domain.communication.dto.response.CommunicationLogResponse;

import java.time.LocalDate;

public interface CommunicationLogService {
    PagedResult<CommunicationLogResponse> search(String clientId,
                                                 String projectId,
                                                 LocalDate dateFrom,
                                                 LocalDate dateTo,
                                                 Integer page,
                                                 Integer size);

    CommunicationLogResponse create(CommunicationLogCreateRequest request);

    CommunicationLogResponse update(String id, CommunicationLogUpdateRequest request);

    void delete(String id);
}
