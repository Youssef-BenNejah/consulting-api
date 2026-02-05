package brama.consultant_business_api.service.userstory.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryCreateRequest;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryUpdateRequest;
import brama.consultant_business_api.domain.userstory.dto.response.UserStoryResponse;
import brama.consultant_business_api.domain.userstory.mapper.UserStoryMapper;
import brama.consultant_business_api.domain.userstory.model.UserStory;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.UserStoryRepository;
import brama.consultant_business_api.service.userstory.UserStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStoryServiceImpl implements UserStoryService {
    private final UserStoryRepository repository;
    private final MongoTemplate mongoTemplate;
    private final UserStoryMapper mapper;

    @Override
    public PagedResult<UserStoryResponse> search(final String projectId,
                                                 final String epicId,
                                                 final StoryStatus status,
                                                 final Priority priority,
                                                 final Integer page,
                                                 final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addIfNotBlank(query, "epicId", epicId);
        QueryUtils.addIfNotNull(query, "status", status);
        QueryUtils.addIfNotNull(query, "priority", priority);
        final long total = mongoTemplate.count(query, UserStory.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<UserStoryResponse> items = mongoTemplate.find(query, UserStory.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<UserStoryResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public UserStoryResponse create(final UserStoryCreateRequest request) {
        final UserStory story = mapper.toEntity(request);
        final UserStory saved = repository.save(story);
        return mapper.toResponse(saved);
    }

    @Override
    public UserStoryResponse update(final String id, final UserStoryUpdateRequest request) {
        final UserStory story = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User story not found: " + id));
        mapper.merge(story, request);
        final UserStory saved = repository.save(story);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("User story not found: " + id);
        }
        repository.deleteById(id);
    }
}
