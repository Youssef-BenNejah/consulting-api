package brama.consultant_business_api.service.task.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.dto.request.TaskCreateRequest;
import brama.consultant_business_api.domain.task.dto.request.TaskUpdateRequest;
import brama.consultant_business_api.domain.task.dto.response.TaskResponse;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import brama.consultant_business_api.domain.task.mapper.TaskMapper;
import brama.consultant_business_api.domain.task.model.Task;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.TaskRepository;
import brama.consultant_business_api.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final MongoTemplate mongoTemplate;
    private final TaskMapper mapper;

    @Override
    public PagedResult<TaskResponse> search(final String projectId,
                                            final TaskStatus status,
                                            final Priority priority,
                                            final OwnerType ownerType,
                                            final Integer page,
                                            final Integer size) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addIfNotNull(query, "status", status);
        QueryUtils.addIfNotNull(query, "priority", priority);
        QueryUtils.addIfNotNull(query, "ownerType", ownerType);
        final long total = mongoTemplate.count(query, Task.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<TaskResponse> items = mongoTemplate.find(query, Task.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<TaskResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public TaskResponse create(final TaskCreateRequest request) {
        final Task task = mapper.toEntity(request);
        final Task saved = repository.save(task);
        return mapper.toResponse(saved);
    }

    @Override
    public TaskResponse update(final String id, final TaskUpdateRequest request) {
        final Task task = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
        mapper.merge(task, request);
        final Task saved = repository.save(task);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Task not found: " + id);
        }
        repository.deleteById(id);
    }
}
