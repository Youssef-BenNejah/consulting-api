package brama.consultant_business_api.service.project.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.common.SortUtils;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.enums.ProjectType;
import brama.consultant_business_api.domain.project.mapper.ProjectMapper;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;
    private final MongoTemplate mongoTemplate;
    private final ProjectMapper mapper;

    @Override
    public PagedResult<ProjectResponse> search(final String search,
                                               final ProjectStatus status,
                                               final String clientId,
                                               final ProjectType type,
                                               final HealthStatus healthStatus,
                                               final LocalDate dateFrom,
                                               final LocalDate dateTo,
                                               final Integer page,
                                               final Integer size,
                                               final String sort) {
        final Query query = new Query();
        QueryUtils.addRegexOrCriteria(query, search, "name", "description", "clientName", "projectId");
        QueryUtils.addIfNotNull(query, "status", status);
        QueryUtils.addIfNotBlank(query, "clientId", clientId);
        QueryUtils.addIfNotNull(query, "type", type);
        QueryUtils.addIfNotNull(query, "healthStatus", healthStatus);

        if (dateFrom != null) {
            query.addCriteria(Criteria.where("endDate").gte(dateFrom));
        }
        if (dateTo != null) {
            query.addCriteria(Criteria.where("startDate").lte(dateTo));
        }

        final long total = mongoTemplate.count(query, Project.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, SortUtils.parseSort(sort));
        query.with(pageable);
        final List<ProjectResponse> items = mongoTemplate.find(query, Project.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<ProjectResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public ProjectResponse create(final ProjectCreateRequest request) {
        final Project project = mapper.toEntity(request);
        final Project saved = repository.save(project);
        return mapper.toResponse(saved);
    }

    @Override
    public ProjectResponse getById(final String id) {
        final Project project = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        return mapper.toResponse(project);
    }

    @Override
    public ProjectResponse update(final String id, final ProjectUpdateRequest request) {
        final Project project = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        mapper.merge(project, request);
        final Project saved = repository.save(project);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Project not found: " + id);
        }
        repository.deleteById(id);
    }
}
