package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeUpdateRequest;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;
import brama.consultant_business_api.service.projecttype.ProjectTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project-types")
@RequiredArgsConstructor
@Tag(name = "Project Types", description = "Project type API")
public class ProjectTypeController {
    private final ProjectTypeService service;

    @GetMapping
    public ApiResponse<List<ProjectTypeResponse>> listProjectTypes() {
        return ApiResponse.ok(service.list());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjectTypeResponse> createProjectType(@Valid @RequestBody final ProjectTypeCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectTypeResponse> getProjectType(@PathVariable final String id) {
        return ApiResponse.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProjectTypeResponse> updateProjectType(@PathVariable final String id,
                                                              @Valid @RequestBody final ProjectTypeUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectType(@PathVariable final String id) {
        service.delete(id);
    }
}
