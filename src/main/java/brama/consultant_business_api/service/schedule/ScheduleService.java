package brama.consultant_business_api.service.schedule;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;
import brama.consultant_business_api.domain.schedule.dto.response.ScheduleResponse;

import java.time.LocalDate;

public interface ScheduleService {
    PagedResult<ScheduleResponse> search(String search,
                                         String clientId,
                                         String projectId,
                                         String projectStatusId,
                                         LocalDate dateFrom,
                                         LocalDate dateTo,
                                         Integer page,
                                         Integer size);

    ScheduleResponse create(ScheduleCreateRequest request);

    ScheduleResponse getById(String id);

    ScheduleResponse update(String id, ScheduleUpdateRequest request);

    void delete(String id);

    byte[] exportCsv(String search,
                     String clientId,
                     String projectId,
                     String projectStatusId,
                     LocalDate dateFrom,
                     LocalDate dateTo);
}
