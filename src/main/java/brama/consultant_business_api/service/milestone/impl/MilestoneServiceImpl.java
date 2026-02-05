package brama.consultant_business_api.service.milestone.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneCreateRequest;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneUpdateRequest;
import brama.consultant_business_api.domain.milestone.dto.response.MilestoneResponse;
import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
import brama.consultant_business_api.domain.milestone.mapper.MilestoneMapper;
import brama.consultant_business_api.domain.milestone.model.Milestone;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.MilestoneRepository;
import brama.consultant_business_api.service.milestone.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneRepository repository;
    private final MongoTemplate mongoTemplate;
    private final MilestoneMapper mapper;

    @Override
    public PagedResult<MilestoneResponse> search(final String projectId,
                                                 final MilestoneStatus status,
                                                 final Integer page,
                                                 final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addIfNotNull(query, "status", status);
        final long total = mongoTemplate.count(query, Milestone.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<MilestoneResponse> items = mongoTemplate.find(query, Milestone.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<MilestoneResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public MilestoneResponse create(final MilestoneCreateRequest request) {
        final Milestone milestone = mapper.toEntity(request);
        final Milestone saved = repository.save(milestone);
        return mapper.toResponse(saved);
    }

    @Override
    public MilestoneResponse update(final String id, final MilestoneUpdateRequest request) {
        final Milestone milestone = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Milestone not found: " + id));
        mapper.merge(milestone, request);
        final Milestone saved = repository.save(milestone);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Milestone not found: " + id);
        }
        repository.deleteById(id);
    }
}
