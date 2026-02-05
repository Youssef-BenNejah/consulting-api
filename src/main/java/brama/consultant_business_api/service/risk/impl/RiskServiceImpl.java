package brama.consultant_business_api.service.risk.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.risk.dto.request.RiskCreateRequest;
import brama.consultant_business_api.domain.risk.dto.request.RiskUpdateRequest;
import brama.consultant_business_api.domain.risk.dto.response.RiskResponse;
import brama.consultant_business_api.domain.risk.enums.RiskStatus;
import brama.consultant_business_api.domain.risk.mapper.RiskMapper;
import brama.consultant_business_api.domain.risk.model.Risk;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.RiskRepository;
import brama.consultant_business_api.service.risk.RiskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiskServiceImpl implements RiskService {
    private final RiskRepository repository;
    private final MongoTemplate mongoTemplate;
    private final RiskMapper mapper;

    @Override
    public PagedResult<RiskResponse> search(final String projectId,
                                            final RiskStatus status,
                                            final Double minScore,
                                            final Integer page,
                                            final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addIfNotNull(query, "status", status);
        if (minScore != null) {
            query.addCriteria(Criteria.where("score").gte(minScore));
        }
        final long total = mongoTemplate.count(query, Risk.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<RiskResponse> items = mongoTemplate.find(query, Risk.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<RiskResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public RiskResponse create(final RiskCreateRequest request) {
        final Risk risk = mapper.toEntity(request);
        final Risk saved = repository.save(risk);
        return mapper.toResponse(saved);
    }

    @Override
    public RiskResponse update(final String id, final RiskUpdateRequest request) {
        final Risk risk = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Risk not found: " + id));
        mapper.merge(risk, request);
        final Risk saved = repository.save(risk);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Risk not found: " + id);
        }
        repository.deleteById(id);
    }
}
