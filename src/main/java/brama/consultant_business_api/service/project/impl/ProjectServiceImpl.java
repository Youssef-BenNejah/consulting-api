package brama.consultant_business_api.service.project.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.common.SortUtils;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.mapper.ProjectMapper;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;
import brama.consultant_business_api.domain.projecttype.mapper.ProjectTypeMapper;
import brama.consultant_business_api.domain.contracttype.dto.response.ContractTypeResponse;
import brama.consultant_business_api.domain.contracttype.mapper.ContractTypeMapper;
import brama.consultant_business_api.domain.contracttype.model.ContractTypeConfig;
import brama.consultant_business_api.domain.projecttype.model.ProjectTypeConfig;
import brama.consultant_business_api.domain.settings.catalog.SettingsCatalogDefaults;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsPriority;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsProjectStatus;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsTag;
import brama.consultant_business_api.domain.settings.dto.response.items.PrioritySettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.ProjectStatusSettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.TagSettingsResponse;
import brama.consultant_business_api.exception.RequestValidationException;
import brama.consultant_business_api.common.ApiError;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ContractTypeRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.repository.ProjectTypeRepository;
import brama.consultant_business_api.repository.SettingsCatalogRepository;
import brama.consultant_business_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;
    private final MongoTemplate mongoTemplate;
    private final ProjectMapper mapper;
    private final SettingsCatalogRepository settingsCatalogRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final ProjectTypeMapper projectTypeMapper;
    private final ContractTypeMapper contractTypeMapper;

    @Override
    public PagedResult<ProjectResponse> search(final String search,
                                               final String statusId,
                                               final String clientId,
                                               final String projectTypeId,
                                               final String priorityId,
                                               final String tagId,
                                               final HealthStatus healthStatus,
                                               final LocalDate dateFrom,
                                               final LocalDate dateTo,
                                               final Integer page,
                                               final Integer size,
                                               final String sort) {
        final Query query = new Query();
        QueryUtils.addRegexOrCriteria(query, search, "name", "description", "clientName", "projectId");
        QueryUtils.addIfNotBlank(query, "statusId", statusId);
        QueryUtils.addIfNotBlank(query, "clientId", clientId);
        QueryUtils.addIfNotBlank(query, "projectTypeId", projectTypeId);
        QueryUtils.addIfNotBlank(query, "priorityId", priorityId);
        if (tagId != null && !tagId.isBlank()) {
            query.addCriteria(Criteria.where("tagIds").in(tagId));
        }
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
        final List<Project> projects = mongoTemplate.find(query, Project.class);
        final SettingsLookups lookups = loadLookups();
        final List<ProjectResponse> items = projects.stream()
                .map(project -> enrich(project, mapper.toResponse(project), lookups))
                .collect(Collectors.toList());
        return PagedResult.<ProjectResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public ProjectResponse create(final ProjectCreateRequest request) {
        validateProjectSettings(request);
        final Project project = mapper.toEntity(request);
        final Project saved = repository.save(project);
        return enrich(saved, mapper.toResponse(saved), loadLookups());
    }

    @Override
    public ProjectResponse getById(final String id) {
        final Project project = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        return enrich(project, mapper.toResponse(project), loadLookups());
    }

    @Override
    public ProjectResponse update(final String id, final ProjectUpdateRequest request) {
        final Project project = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        validateProjectSettings(request);
        mapper.merge(project, request);
        final Project saved = repository.save(project);
        return enrich(saved, mapper.toResponse(saved), loadLookups());
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Project not found: " + id);
        }
        repository.deleteById(id);
    }

    private void validateProjectSettings(final ProjectCreateRequest request) {
        if (request == null) {
            return;
        }
        final List<ApiError> errors = new ArrayList<>();
        final SettingsCatalog catalog = getCatalog();
        if (!existsStatus(catalog, request.getStatusId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Project status not found: " + request.getStatusId()).field("statusId").build());
        }
        if (!projectTypeRepository.existsById(request.getProjectTypeId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Project type not found: " + request.getProjectTypeId()).field("projectTypeId").build());
        }
        if (request.getPriorityId() != null && !request.getPriorityId().isBlank() && !existsPriority(catalog, request.getPriorityId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Priority not found: " + request.getPriorityId()).field("priorityId").build());
        }
        if (request.getContractTypeId() != null && !request.getContractTypeId().isBlank()
                && !contractTypeRepository.existsById(request.getContractTypeId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Contract type not found: " + request.getContractTypeId()).field("contractTypeId").build());
        }
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            final Set<String> allowed = catalog.getTags() == null ? Set.of() : catalog.getTags().stream()
                    .map(SettingsTag::getId)
                    .collect(Collectors.toSet());
            for (String tagId : request.getTagIds()) {
                if (tagId == null || tagId.isBlank() || !allowed.contains(tagId)) {
                    errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Tag not found: " + tagId).field("tagIds").build());
                    break;
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new RequestValidationException(errors);
        }
    }

    private void validateProjectSettings(final ProjectUpdateRequest request) {
        if (request == null) {
            return;
        }
        final List<ApiError> errors = new ArrayList<>();
        final SettingsCatalog catalog = getCatalog();
        if (request.getStatusId() != null && !existsStatus(catalog, request.getStatusId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Project status not found: " + request.getStatusId()).field("statusId").build());
        }
        if (request.getProjectTypeId() != null && !projectTypeRepository.existsById(request.getProjectTypeId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Project type not found: " + request.getProjectTypeId()).field("projectTypeId").build());
        }
        if (request.getPriorityId() != null && !request.getPriorityId().isBlank() && !existsPriority(catalog, request.getPriorityId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Priority not found: " + request.getPriorityId()).field("priorityId").build());
        }
        if (request.getContractTypeId() != null && !request.getContractTypeId().isBlank()
                && !contractTypeRepository.existsById(request.getContractTypeId())) {
            errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Contract type not found: " + request.getContractTypeId()).field("contractTypeId").build());
        }
        if (request.getTagIds() != null) {
            final Set<String> allowed = catalog.getTags() == null ? Set.of() : catalog.getTags().stream()
                    .map(SettingsTag::getId)
                    .collect(Collectors.toSet());
            for (String tagId : request.getTagIds()) {
                if (tagId == null || tagId.isBlank() || !allowed.contains(tagId)) {
                    errors.add(ApiError.builder().code("SETTING_NOT_FOUND").message("Tag not found: " + tagId).field("tagIds").build());
                    break;
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new RequestValidationException(errors);
        }
    }

    private SettingsCatalog getCatalog() {
        return settingsCatalogRepository.findById(SettingsCatalogDefaults.CATALOG_ID)
                .orElseGet(SettingsCatalogDefaults::defaultCatalog);
    }

    private boolean existsStatus(final SettingsCatalog catalog, final String id) {
        if (id == null || id.isBlank() || catalog == null || catalog.getProjectStatuses() == null) {
            return false;
        }
        for (SettingsProjectStatus status : catalog.getProjectStatuses()) {
            if (id.equals(status.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean existsPriority(final SettingsCatalog catalog, final String id) {
        if (id == null || id.isBlank() || catalog == null || catalog.getPriorities() == null) {
            return false;
        }
        for (SettingsPriority priority : catalog.getPriorities()) {
            if (id.equals(priority.getId())) {
                return true;
            }
        }
        return false;
    }

    private SettingsLookups loadLookups() {
        final SettingsCatalog catalog = getCatalog();
        final Map<String, ProjectStatusSettingsResponse> statuses = catalog.getProjectStatuses() == null
                ? Map.of()
                : catalog.getProjectStatuses().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        SettingsProjectStatus::getId,
                        item -> ProjectStatusSettingsResponse.builder()
                                .id(item.getId())
                                .key(item.getKey())
                                .name(item.getName())
                                .color(item.getColor())
                                .category(item.getCategory())
                                .build(),
                        (a, b) -> a
                ));

        final Map<String, PrioritySettingsResponse> priorities = catalog.getPriorities() == null
                ? Map.of()
                : catalog.getPriorities().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        SettingsPriority::getId,
                        item -> PrioritySettingsResponse.builder()
                                .id(item.getId())
                                .key(item.getKey())
                                .name(item.getName())
                                .color(item.getColor())
                                .build(),
                        (a, b) -> a
                ));

        final Map<String, TagSettingsResponse> tags = catalog.getTags() == null
                ? Map.of()
                : catalog.getTags().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        SettingsTag::getId,
                        item -> TagSettingsResponse.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .color(item.getColor())
                                .build(),
                        (a, b) -> a
                ));

        final Map<String, ProjectTypeResponse> projectTypes = projectTypeRepository.findAll().stream()
                .collect(Collectors.toMap(
                        ProjectTypeConfig::getId,
                        projectTypeMapper::toResponse,
                        (a, b) -> a
                ));

        final Map<String, ContractTypeResponse> contractTypes = contractTypeRepository.findAll().stream()
                .collect(Collectors.toMap(
                        ContractTypeConfig::getId,
                        contractTypeMapper::toResponse,
                        (a, b) -> a
                ));

        return new SettingsLookups(statuses, priorities, tags, projectTypes, contractTypes);
    }

    private ProjectResponse enrich(final Project project,
                                   final ProjectResponse response,
                                   final SettingsLookups lookups) {
        if (response == null || lookups == null || project == null) {
            return response;
        }
        response.setStatus(lookups.statuses.get(project.getStatusId()));
        response.setProjectType(lookups.projectTypes.get(project.getProjectTypeId()));
        response.setPriority(lookups.priorities.get(project.getPriorityId()));
        if (project.getTagIds() != null) {
            response.setTags(project.getTagIds().stream()
                    .map(lookups.tags::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }
        response.setContractType(lookups.contractTypes.get(project.getContractTypeId()));
        return response;
    }

    private record SettingsLookups(Map<String, ProjectStatusSettingsResponse> statuses,
                                   Map<String, PrioritySettingsResponse> priorities,
                                   Map<String, TagSettingsResponse> tags,
                                   Map<String, ProjectTypeResponse> projectTypes,
                                   Map<String, ContractTypeResponse> contractTypes) {
    }
}
