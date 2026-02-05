package brama.consultant_business_api.service.opportunity.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityCreateRequest;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityUpdateRequest;
import brama.consultant_business_api.domain.opportunity.dto.response.OpportunityResponse;
import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;
import brama.consultant_business_api.domain.opportunity.mapper.OpportunityMapper;
import brama.consultant_business_api.domain.opportunity.model.Opportunity;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.OpportunityRepository;
import brama.consultant_business_api.service.opportunity.OpportunityService;
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
public class OpportunityServiceImpl implements OpportunityService {
    private final OpportunityRepository repository;
    private final MongoTemplate mongoTemplate;
    private final OpportunityMapper mapper;

    @Override
    public PagedResult<OpportunityResponse> search(final String clientId,
                                                   final OpportunityStage stage,
                                                   final LocalDate expectedCloseFrom,
                                                   final LocalDate expectedCloseTo,
                                                   final Integer page,
                                                   final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "clientId", clientId);
        QueryUtils.addIfNotNull(query, "stage", stage);
        QueryUtils.addDateRange(query, "expectedCloseDate", expectedCloseFrom, expectedCloseTo);
        final long total = mongoTemplate.count(query, Opportunity.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<OpportunityResponse> items = mongoTemplate.find(query, Opportunity.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<OpportunityResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public OpportunityResponse create(final OpportunityCreateRequest request) {
        final Opportunity opportunity = mapper.toEntity(request);
        final Opportunity saved = repository.save(opportunity);
        return mapper.toResponse(saved);
    }

    @Override
    public OpportunityResponse update(final String id, final OpportunityUpdateRequest request) {
        final Opportunity opportunity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opportunity not found: " + id));
        mapper.merge(opportunity, request);
        final Opportunity saved = repository.save(opportunity);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Opportunity not found: " + id);
        }
        repository.deleteById(id);
    }
}
