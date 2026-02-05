package brama.consultant_business_api.service.epic.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.epic.dto.request.EpicCreateRequest;
import brama.consultant_business_api.domain.epic.dto.request.EpicUpdateRequest;
import brama.consultant_business_api.domain.epic.dto.response.EpicResponse;
import brama.consultant_business_api.domain.epic.mapper.EpicMapper;
import brama.consultant_business_api.domain.epic.model.Epic;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.EpicRepository;
import brama.consultant_business_api.service.epic.EpicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpicServiceImpl implements EpicService {
    private final EpicRepository repository;
    private final MongoTemplate mongoTemplate;
    private final EpicMapper mapper;

    @Override
    public PagedResult<EpicResponse> search(final String projectId,
                                            final StoryStatus status,
                                            final Priority priority,
                                            final Integer page,
                                            final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addIfNotNull(query, "status", status);
        QueryUtils.addIfNotNull(query, "priority", priority);
        final long total = mongoTemplate.count(query, Epic.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<EpicResponse> items = mongoTemplate.find(query, Epic.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<EpicResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public EpicResponse create(final EpicCreateRequest request) {
        final Epic epic = mapper.toEntity(request);
        final Epic saved = repository.save(epic);
        return mapper.toResponse(saved);
    }

    @Override
    public EpicResponse update(final String id, final EpicUpdateRequest request) {
        final Epic epic = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found: " + id));
        mapper.merge(epic, request);
        final Epic saved = repository.save(epic);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Epic not found: " + id);
        }
        repository.deleteById(id);
    }
}
