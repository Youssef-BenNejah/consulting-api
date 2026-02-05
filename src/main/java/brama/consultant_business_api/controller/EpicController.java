package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import brama.consultant_business_api.domain.epic.dto.request.EpicCreateRequest;
import brama.consultant_business_api.domain.epic.dto.request.EpicUpdateRequest;
import brama.consultant_business_api.domain.epic.dto.response.EpicResponse;
import brama.consultant_business_api.service.epic.EpicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/epics")
@RequiredArgsConstructor
@Tag(name = "Epics", description = "Epic API")
public class EpicController {
    private final EpicService service;

    @GetMapping
    public ApiResponse<List<EpicResponse>> listEpics(
            @RequestParam(required = false) final String projectId,
            @RequestParam(required = false) final StoryStatus status,
            @RequestParam(required = false) final Priority priority,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<EpicResponse> result = service.search(projectId, status, priority, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EpicResponse> createEpic(@Valid @RequestBody final EpicCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<EpicResponse> updateEpic(@PathVariable final String id,
                                                @Valid @RequestBody final EpicUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEpic(@PathVariable final String id) {
        service.delete(id);
    }
}
