package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.dto.request.TaskCreateRequest;
import brama.consultant_business_api.domain.task.dto.request.TaskUpdateRequest;
import brama.consultant_business_api.domain.task.dto.response.TaskResponse;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import brama.consultant_business_api.service.task.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task API")
public class TaskController {
    private final TaskService service;

    @GetMapping
    public ApiResponse<List<TaskResponse>> listTasks(
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final TaskStatus status,
            @RequestParam(required = false) final Priority priority,
            @RequestParam(required = false) final OwnerType ownerType,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<TaskResponse> result = service.search(projectId, status, priority, ownerType, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TaskResponse> createTask(@Valid @RequestBody final TaskCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<TaskResponse> updateTask(@PathVariable final String id,
                                                @Valid @RequestBody final TaskUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable final String id) {
        service.delete(id);
    }
}
