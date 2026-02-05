package brama.consultant_business_api.service.issue.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.issue.dto.request.IssueCreateRequest;
import brama.consultant_business_api.domain.issue.dto.request.IssueUpdateRequest;
import brama.consultant_business_api.domain.issue.dto.response.IssueResponse;
import brama.consultant_business_api.domain.issue.enums.IssueSeverity;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
import brama.consultant_business_api.domain.issue.mapper.IssueMapper;
import brama.consultant_business_api.domain.issue.model.Issue;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.IssueRepository;
import brama.consultant_business_api.service.issue.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRepository repository;
    private final MongoTemplate mongoTemplate;
    private final IssueMapper mapper;

    @Override
    public PagedResult<IssueResponse> search(final String projectId,
                                             final IssueStatus status,
                                             final IssueSeverity severity,
                                             final Integer page,
                                             final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addIfNotNull(query, "status", status);
        QueryUtils.addIfNotNull(query, "severity", severity);
        final long total = mongoTemplate.count(query, Issue.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<IssueResponse> items = mongoTemplate.find(query, Issue.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<IssueResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public IssueResponse create(final IssueCreateRequest request) {
        final Issue issue = mapper.toEntity(request);
        final Issue saved = repository.save(issue);
        return mapper.toResponse(saved);
    }

    @Override
    public IssueResponse update(final String id, final IssueUpdateRequest request) {
        final Issue issue = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found: " + id));
        mapper.merge(issue, request);
        final Issue saved = repository.save(issue);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Issue not found: " + id);
        }
        repository.deleteById(id);
    }
}
