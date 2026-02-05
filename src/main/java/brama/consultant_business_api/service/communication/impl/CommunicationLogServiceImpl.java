package brama.consultant_business_api.service.communication.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogCreateRequest;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogUpdateRequest;
import brama.consultant_business_api.domain.communication.dto.response.CommunicationLogResponse;
import brama.consultant_business_api.domain.communication.mapper.CommunicationLogMapper;
import brama.consultant_business_api.domain.communication.model.CommunicationLog;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.CommunicationLogRepository;
import brama.consultant_business_api.service.communication.CommunicationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunicationLogServiceImpl implements CommunicationLogService {
    private final CommunicationLogRepository repository;
    private final MongoTemplate mongoTemplate;
    private final CommunicationLogMapper mapper;

    @Override
    public PagedResult<CommunicationLogResponse> search(final String clientId,
                                                        final String projectId,
                                                        final LocalDate dateFrom,
                                                        final LocalDate dateTo,
                                                        final Integer page,
                                                        final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "clientId", clientId);
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addDateRange(query, "date", dateFrom, dateTo);
        final long total = mongoTemplate.count(query, CommunicationLog.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<CommunicationLogResponse> items = mongoTemplate.find(query, CommunicationLog.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<CommunicationLogResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public CommunicationLogResponse create(final CommunicationLogCreateRequest request) {
        final CommunicationLog log = mapper.toEntity(request);
        final CommunicationLog saved = repository.save(log);
        return mapper.toResponse(saved);
    }

    @Override
    public CommunicationLogResponse update(final String id, final CommunicationLogUpdateRequest request) {
        final CommunicationLog log = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Communication log not found: " + id));
        mapper.merge(log, request);
        final CommunicationLog saved = repository.save(log);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Communication log not found: " + id);
        }
        repository.deleteById(id);
    }
}
