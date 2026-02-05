package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryCreateRequest;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryUpdateRequest;
import brama.consultant_business_api.domain.userstory.dto.response.UserStoryResponse;
import brama.consultant_business_api.service.userstory.UserStoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-stories")
@RequiredArgsConstructor
@Tag(name = "User Stories", description = "User story API")
public class UserStoryController {
    private final UserStoryService service;

    @GetMapping
    public ApiResponse<List<UserStoryResponse>> listUserStories(
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final String epicId,
            @RequestParam(required = false) final StoryStatus status,
            @RequestParam(required = false) final Priority priority,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<UserStoryResponse> result = service.search(projectId, epicId, status, priority, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserStoryResponse> createUserStory(@Valid @RequestBody final UserStoryCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserStoryResponse> updateUserStory(@PathVariable final String id,
                                                          @Valid @RequestBody final UserStoryUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserStory(@PathVariable final String id) {
        service.delete(id);
    }
}
