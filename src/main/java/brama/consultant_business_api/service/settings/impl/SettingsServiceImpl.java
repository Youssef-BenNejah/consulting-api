package brama.consultant_business_api.service.settings.impl;

import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeUpdateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryUpdateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeUpdateRequest;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsPriority;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsProjectStatus;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsTag;
import brama.consultant_business_api.domain.settings.dto.request.SettingsPatchRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ContractTypeUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.DocumentCategoryUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ProjectTypeUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ProjectStatusUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.PriorityUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.RoleUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.TagUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.response.SettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.PrioritySettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.ProjectStatusSettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.RoleSettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.TagSettingsResponse;
import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsPatchRequest;
import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsPatchRequest;
import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsUpdateRequest;
import brama.consultant_business_api.common.ApiError;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.exception.RequestValidationException;
import brama.consultant_business_api.repository.SettingsCatalogRepository;
import brama.consultant_business_api.role.Role;
import brama.consultant_business_api.role.RoleRepository;
import brama.consultant_business_api.service.contracttype.ContractTypeService;
import brama.consultant_business_api.service.documentcategory.DocumentCategoryService;
import brama.consultant_business_api.service.projecttype.ProjectTypeService;
import brama.consultant_business_api.service.settings.GeneralSettingsService;
import brama.consultant_business_api.service.settings.NotificationSettingsService;
import brama.consultant_business_api.service.settings.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
    private static final String CATALOG_ID = "catalog";

    private final DocumentCategoryService documentCategoryService;
    private final ProjectTypeService projectTypeService;
    private final ContractTypeService contractTypeService;
    private final GeneralSettingsService generalSettingsService;
    private final NotificationSettingsService notificationSettingsService;
    private final SettingsCatalogRepository settingsCatalogRepository;
    private final RoleRepository roleRepository;

    @Override
    public SettingsResponse getAll() {
        SettingsCatalog catalog = getCatalogForRead();
        return SettingsResponse.builder()
                .documentCategories(documentCategoryService.list())
                .projectTypes(projectTypeService.list())
                .contractTypes(contractTypeService.list())
                .projectStatuses(toProjectStatusResponses(catalog.getProjectStatuses()))
                .priorities(toPriorityResponses(catalog.getPriorities()))
                .tags(toTagResponses(catalog.getTags()))
                .roles(toRoleResponses(roleRepository.findAll()))
                .general(generalSettingsService.get())
                .notifications(notificationSettingsService.get())
                .build();
    }

    @Override
    public SettingsResponse patch(final SettingsPatchRequest request) {
        if (request == null) {
            return getAll();
        }
        validatePatchRequest(request);

        if (isTrue(request.getReplaceDocumentCategories())) {
            documentCategoryService.deleteAll();
        } else {
            deleteIfPresent(request.getDocumentCategoryDeletes(), documentCategoryService::delete);
        }
        if (isTrue(request.getReplaceProjectTypes())) {
            projectTypeService.deleteAll();
        } else {
            deleteIfPresent(request.getProjectTypeDeletes(), projectTypeService::delete);
        }
        if (isTrue(request.getReplaceContractTypes())) {
            contractTypeService.deleteAll();
        } else {
            deleteIfPresent(request.getContractTypeDeletes(), contractTypeService::delete);
        }

        if (request.getDocumentCategories() != null) {
            upsertDocumentCategories(request.getDocumentCategories());
        }
        if (request.getProjectTypes() != null) {
            upsertProjectTypes(request.getProjectTypes());
        }
        if (request.getContractTypes() != null) {
            upsertContractTypes(request.getContractTypes());
        }
        if (request.getProjectStatusDeletes() != null
                || request.getPriorityDeletes() != null
                || request.getTagDeletes() != null
                || request.getProjectStatuses() != null
                || request.getPriorities() != null
                || request.getTags() != null
                || isTrue(request.getReplaceProjectStatuses())
                || isTrue(request.getReplacePriorities())
                || isTrue(request.getReplaceTags())) {
            SettingsCatalog catalog = getCatalogForWrite();
            if (isTrue(request.getReplaceProjectStatuses())) {
                catalog.setProjectStatuses(new ArrayList<>());
            } else {
                deleteIfPresent(request.getProjectStatusDeletes(), id -> removeById(catalog.getProjectStatuses(), id));
            }
            if (isTrue(request.getReplacePriorities())) {
                catalog.setPriorities(new ArrayList<>());
            } else {
                deleteIfPresent(request.getPriorityDeletes(), id -> removeById(catalog.getPriorities(), id));
            }
            if (isTrue(request.getReplaceTags())) {
                catalog.setTags(new ArrayList<>());
            } else {
                deleteIfPresent(request.getTagDeletes(), id -> removeById(catalog.getTags(), id));
            }
            upsertProjectStatuses(catalog, request.getProjectStatuses());
            upsertPriorities(catalog, request.getPriorities());
            upsertTags(catalog, request.getTags());
            settingsCatalogRepository.save(catalog);
        }
        if (isTrue(request.getReplaceRoles())) {
            roleRepository.deleteAll();
        } else {
            deleteIfPresent(request.getRoleDeletes(), roleRepository::deleteById);
        }
        if (request.getRoles() != null) {
            upsertRoles(request.getRoles());
        }
        if (request.getGeneral() != null) {
            generalSettingsService.update(toGeneralUpdate(request.getGeneral()));
        }
        if (request.getNotifications() != null) {
            notificationSettingsService.update(toNotificationUpdate(request.getNotifications()));
        }

        return getAll();
    }

    private void upsertDocumentCategories(final List<DocumentCategoryUpsertRequest> items) {
        for (DocumentCategoryUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                DocumentCategoryCreateRequest create = DocumentCategoryCreateRequest.builder()
                        .name(item.getName())
                        .key(item.getKey())
                        .color(item.getColor())
                        .build();
                documentCategoryService.create(create);
            } else {
                DocumentCategoryUpdateRequest update = DocumentCategoryUpdateRequest.builder()
                        .name(item.getName())
                        .key(item.getKey())
                        .color(item.getColor())
                        .build();
                try {
                    documentCategoryService.update(item.getId(), update);
                } catch (EntityNotFoundException ex) {
                    DocumentCategoryCreateRequest create = DocumentCategoryCreateRequest.builder()
                            .name(item.getName())
                            .key(item.getKey())
                            .color(item.getColor())
                            .build();
                    documentCategoryService.create(create);
                }
            }
        }
    }

    private void upsertProjectTypes(final List<ProjectTypeUpsertRequest> items) {
        for (ProjectTypeUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                ProjectTypeCreateRequest create = ProjectTypeCreateRequest.builder()
                        .name(item.getName())
                        .key(item.getKey())
                        .description(item.getDescription())
                        .build();
                projectTypeService.create(create);
            } else {
                ProjectTypeUpdateRequest update = ProjectTypeUpdateRequest.builder()
                        .name(item.getName())
                        .key(item.getKey())
                        .description(item.getDescription())
                        .build();
                try {
                    projectTypeService.update(item.getId(), update);
                } catch (EntityNotFoundException ex) {
                    ProjectTypeCreateRequest create = ProjectTypeCreateRequest.builder()
                            .name(item.getName())
                            .key(item.getKey())
                            .description(item.getDescription())
                            .build();
                    projectTypeService.create(create);
                }
            }
        }
    }

    private void upsertContractTypes(final List<ContractTypeUpsertRequest> items) {
        for (ContractTypeUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                ContractTypeCreateRequest create = ContractTypeCreateRequest.builder()
                        .name(item.getName())
                        .key(item.getKey())
                        .description(item.getDescription())
                        .build();
                contractTypeService.create(create);
            } else {
                ContractTypeUpdateRequest update = ContractTypeUpdateRequest.builder()
                        .name(item.getName())
                        .key(item.getKey())
                        .description(item.getDescription())
                        .build();
                try {
                    contractTypeService.update(item.getId(), update);
                } catch (EntityNotFoundException ex) {
                    ContractTypeCreateRequest create = ContractTypeCreateRequest.builder()
                            .name(item.getName())
                            .key(item.getKey())
                            .description(item.getDescription())
                            .build();
                    contractTypeService.create(create);
                }
            }
        }
    }

    private GeneralSettingsUpdateRequest toGeneralUpdate(final GeneralSettingsPatchRequest request) {
        return GeneralSettingsUpdateRequest.builder()
                .companyName(request.getCompanyName())
                .email(request.getEmail())
                .timezone(request.getTimezone())
                .defaultCurrency(request.getDefaultCurrency())
                .darkMode(request.getDarkMode())
                .compactMode(request.getCompactMode())
                .build();
    }

    private NotificationSettingsUpdateRequest toNotificationUpdate(final NotificationSettingsPatchRequest request) {
        return NotificationSettingsUpdateRequest.builder()
                .emailNotifications(request.getEmailNotifications())
                .slackIntegration(request.getSlackIntegration())
                .milestoneReminders(request.getMilestoneReminders())
                .invoiceDueAlerts(request.getInvoiceDueAlerts())
                .weeklyDigest(request.getWeeklyDigest())
                .build();
    }

    private void deleteIfPresent(final List<String> ids, final java.util.function.Consumer<String> deleteFn) {
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            if (!isBlank(id)) {
                deleteFn.accept(id);
            }
        }
    }

    private boolean isBlank(final String value) {
        return value == null || value.trim().isEmpty();
    }

    private void validatePatchRequest(final SettingsPatchRequest request) {
        List<ApiError> errors = new ArrayList<>();
        validateDocumentCategoryUpserts(request.getDocumentCategories(), errors);
        validateProjectTypeUpserts(request.getProjectTypes(), errors);
        validateContractTypeUpserts(request.getContractTypes(), errors);
        validateProjectStatusUpserts(request.getProjectStatuses(), errors);
        validatePriorityUpserts(request.getPriorities(), errors);
        validateTagUpserts(request.getTags(), errors);
        validateRoleUpserts(request.getRoles(), errors);

        if (!errors.isEmpty()) {
            throw new RequestValidationException(errors);
        }
    }

    private void validateDocumentCategoryUpserts(final List<DocumentCategoryUpsertRequest> items, final List<ApiError> errors) {
        validateUpsertList("documentCategories", items, errors,
                item -> !isBlank(item.getName()) || !isBlank(item.getKey()) || !isBlank(item.getColor()),
                DocumentCategoryUpsertRequest::getId);
        validateRequiredForCreate("documentCategories", items, errors,
                item -> item.getName(),
                item -> item.getKey(),
                item -> item.getColor());
    }

    private void validateProjectTypeUpserts(final List<ProjectTypeUpsertRequest> items, final List<ApiError> errors) {
        validateUpsertList("projectTypes", items, errors,
                item -> !isBlank(item.getName()) || !isBlank(item.getKey()) || !isBlank(item.getDescription()),
                ProjectTypeUpsertRequest::getId);
        validateRequiredForCreate("projectTypes", items, errors,
                ProjectTypeUpsertRequest::getName,
                ProjectTypeUpsertRequest::getKey,
                ProjectTypeUpsertRequest::getDescription);
    }

    private void validateContractTypeUpserts(final List<ContractTypeUpsertRequest> items, final List<ApiError> errors) {
        validateUpsertList("contractTypes", items, errors,
                item -> !isBlank(item.getName()) || !isBlank(item.getKey()) || !isBlank(item.getDescription()),
                ContractTypeUpsertRequest::getId);
        validateRequiredForCreate("contractTypes", items, errors,
                ContractTypeUpsertRequest::getName,
                ContractTypeUpsertRequest::getKey,
                ContractTypeUpsertRequest::getDescription);
    }

    private void validateProjectStatusUpserts(final List<ProjectStatusUpsertRequest> items, final List<ApiError> errors) {
        validateUpsertList("projectStatuses", items, errors,
                item -> !isBlank(item.getKey()) || !isBlank(item.getName()) || !isBlank(item.getColor()) || !isBlank(item.getCategory()),
                ProjectStatusUpsertRequest::getId);
        validateRequiredForCreate("projectStatuses", items, errors,
                ProjectStatusUpsertRequest::getKey,
                ProjectStatusUpsertRequest::getName,
                ProjectStatusUpsertRequest::getColor,
                ProjectStatusUpsertRequest::getCategory);
    }

    private void validatePriorityUpserts(final List<PriorityUpsertRequest> items, final List<ApiError> errors) {
        validateUpsertList("priorities", items, errors,
                item -> !isBlank(item.getKey()) || !isBlank(item.getName()) || !isBlank(item.getColor()),
                PriorityUpsertRequest::getId);
        validateRequiredForCreate("priorities", items, errors,
                PriorityUpsertRequest::getKey,
                PriorityUpsertRequest::getName,
                PriorityUpsertRequest::getColor);
    }

    private void validateTagUpserts(final List<TagUpsertRequest> items, final List<ApiError> errors) {
        validateUpsertList("tags", items, errors,
                item -> !isBlank(item.getName()) || !isBlank(item.getColor()),
                TagUpsertRequest::getId);
        validateRequiredForCreate("tags", items, errors,
                TagUpsertRequest::getName,
                TagUpsertRequest::getColor);
    }

    private void validateRoleUpserts(final List<RoleUpsertRequest> items, final List<ApiError> errors) {
        validateUpsertList("roles", items, errors,
                item -> !isBlank(item.getName()) || !isBlank(item.getDescription())
                        || (item.getPermissions() != null && !item.getPermissions().isEmpty()),
                RoleUpsertRequest::getId);
        validateRequiredForCreate("roles", items, errors,
                RoleUpsertRequest::getName,
                RoleUpsertRequest::getDescription);
    }

    private <T> void validateUpsertList(final String field,
                                        final List<T> items,
                                        final List<ApiError> errors,
                                        final java.util.function.Predicate<T> hasFields,
                                        final java.util.function.Function<T, String> getId) {
        if (items == null) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            if (item == null) {
                errors.add(ApiError.builder()
                        .code("INVALID_ITEM")
                        .message("Item cannot be null")
                        .field(field + "[" + i + "]")
                        .build());
                continue;
            }
            String id = getId.apply(item);
            if (isBlank(id) && !hasFields.test(item)) {
                errors.add(ApiError.builder()
                        .code("INVALID_ITEM")
                        .message("At least one field is required for create")
                        .field(field + "[" + i + "]")
                        .build());
                continue;
            }
            if (!isBlank(id) && !hasFields.test(item)) {
                errors.add(ApiError.builder()
                        .code("INVALID_ITEM")
                        .message("At least one field is required for update")
                        .field(field + "[" + i + "]")
                        .build());
            }
        }
    }

    @SafeVarargs
    private <T> void validateRequiredForCreate(final String field,
                                               final List<T> items,
                                               final List<ApiError> errors,
                                               final java.util.function.Function<T, String>... requiredFields) {
        if (items == null) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            if (item == null) {
                continue;
            }
            String id = extractId(item);
            if (!isBlank(id)) {
                continue;
            }
            for (java.util.function.Function<T, String> required : requiredFields) {
                String value = required.apply(item);
                if (isBlank(value)) {
                    errors.add(ApiError.builder()
                            .code("INVALID_ITEM")
                            .message("Required field is missing")
                            .field(field + "[" + i + "]")
                            .build());
                    break;
                }
            }
        }
    }

    private SettingsCatalog getCatalogForRead() {
        return settingsCatalogRepository.findById(CATALOG_ID)
                .orElseGet(this::defaultCatalog);
    }

    private SettingsCatalog getCatalogForWrite() {
        return settingsCatalogRepository.findById(CATALOG_ID)
                .orElseGet(() -> {
                    SettingsCatalog catalog = defaultCatalog();
                    catalog.setId(CATALOG_ID);
                    return catalog;
                });
    }

    private SettingsCatalog defaultCatalog() {
        return SettingsCatalog.builder()
                .id(CATALOG_ID)
                .projectStatuses(defaultProjectStatuses())
                .priorities(defaultPriorities())
                .tags(defaultTags())
                .build();
    }

    private List<SettingsProjectStatus> defaultProjectStatuses() {
        List<SettingsProjectStatus> statuses = new ArrayList<>();
        statuses.add(SettingsProjectStatus.builder().id("draft").key("draft").name("Draft").color("bg-muted").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("discovery").key("discovery").name("Discovery").color("bg-primary").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("approved").key("approved").name("Approved").color("bg-health-green").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("delivery").key("delivery").name("Delivery").color("bg-primary").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("review").key("review").name("Review").color("bg-health-amber").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("delivered").key("delivered").name("Delivered").color("bg-health-green").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("closed").key("closed").name("Closed").color("bg-muted").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("on-hold").key("on-hold").name("On Hold").color("bg-muted").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("cancelled").key("cancelled").name("Cancelled").color("bg-health-red").category("project").build());
        return statuses;
    }

    private List<SettingsPriority> defaultPriorities() {
        List<SettingsPriority> priorities = new ArrayList<>();
        priorities.add(SettingsPriority.builder().id("must").key("must").name("Must").color("bg-health-red").build());
        priorities.add(SettingsPriority.builder().id("should").key("should").name("Should").color("bg-health-amber").build());
        priorities.add(SettingsPriority.builder().id("could").key("could").name("Could").color("bg-primary").build());
        return priorities;
    }

    private List<SettingsTag> defaultTags() {
        List<SettingsTag> tags = new ArrayList<>();
        tags.add(SettingsTag.builder().id("enterprise").name("enterprise").color("text-primary").build());
        tags.add(SettingsTag.builder().id("tech").name("tech").color("text-blue-400").build());
        tags.add(SettingsTag.builder().id("strategic").name("strategic").color("text-health-green").build());
        tags.add(SettingsTag.builder().id("finance").name("finance").color("text-health-amber").build());
        tags.add(SettingsTag.builder().id("health").name("health").color("text-pink-400").build());
        tags.add(SettingsTag.builder().id("compliance").name("compliance").color("text-purple-400").build());
        return tags;
    }

    private void upsertProjectStatuses(final SettingsCatalog catalog, final List<ProjectStatusUpsertRequest> items) {
        if (items == null) {
            return;
        }
        List<SettingsProjectStatus> current = ensureList(catalog.getProjectStatuses());
        Map<String, SettingsProjectStatus> byId = indexById(current);
        for (ProjectStatusUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                SettingsProjectStatus created = SettingsProjectStatus.builder()
                        .id(UUID.randomUUID().toString())
                        .key(item.getKey())
                        .name(item.getName())
                        .color(item.getColor())
                        .category(item.getCategory())
                        .build();
                current.add(created);
            } else {
                SettingsProjectStatus existing = byId.get(item.getId());
                if (existing == null) {
                    existing = SettingsProjectStatus.builder().id(item.getId()).build();
                    current.add(existing);
                    byId.put(item.getId(), existing);
                }
                if (item.getKey() != null) {
                    existing.setKey(item.getKey());
                }
                if (item.getName() != null) {
                    existing.setName(item.getName());
                }
                if (item.getColor() != null) {
                    existing.setColor(item.getColor());
                }
                if (item.getCategory() != null) {
                    existing.setCategory(item.getCategory());
                }
            }
        }
        catalog.setProjectStatuses(current);
    }

    private void upsertPriorities(final SettingsCatalog catalog, final List<PriorityUpsertRequest> items) {
        if (items == null) {
            return;
        }
        List<SettingsPriority> current = ensureList(catalog.getPriorities());
        Map<String, SettingsPriority> byId = indexById(current);
        for (PriorityUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                SettingsPriority created = SettingsPriority.builder()
                        .id(UUID.randomUUID().toString())
                        .key(item.getKey())
                        .name(item.getName())
                        .color(item.getColor())
                        .build();
                current.add(created);
            } else {
                SettingsPriority existing = byId.get(item.getId());
                if (existing == null) {
                    existing = SettingsPriority.builder().id(item.getId()).build();
                    current.add(existing);
                    byId.put(item.getId(), existing);
                }
                if (item.getKey() != null) {
                    existing.setKey(item.getKey());
                }
                if (item.getName() != null) {
                    existing.setName(item.getName());
                }
                if (item.getColor() != null) {
                    existing.setColor(item.getColor());
                }
            }
        }
        catalog.setPriorities(current);
    }

    private void upsertTags(final SettingsCatalog catalog, final List<TagUpsertRequest> items) {
        if (items == null) {
            return;
        }
        List<SettingsTag> current = ensureList(catalog.getTags());
        Map<String, SettingsTag> byId = indexById(current);
        for (TagUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                SettingsTag created = SettingsTag.builder()
                        .id(UUID.randomUUID().toString())
                        .name(item.getName())
                        .color(item.getColor())
                        .build();
                current.add(created);
            } else {
                SettingsTag existing = byId.get(item.getId());
                if (existing == null) {
                    existing = SettingsTag.builder().id(item.getId()).build();
                    current.add(existing);
                    byId.put(item.getId(), existing);
                }
                if (item.getName() != null) {
                    existing.setName(item.getName());
                }
                if (item.getColor() != null) {
                    existing.setColor(item.getColor());
                }
            }
        }
        catalog.setTags(current);
    }

    private void upsertRoles(final List<RoleUpsertRequest> items) {
        for (RoleUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                Role created = Role.builder()
                        .name(item.getName())
                        .description(item.getDescription())
                        .permissions(item.getPermissions())
                        .build();
                roleRepository.save(created);
            } else {
                Role role = roleRepository.findById(item.getId())
                        .orElseGet(() -> Role.builder().id(item.getId()).build());
                if (item.getName() != null) {
                    role.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    role.setDescription(item.getDescription());
                }
                if (item.getPermissions() != null) {
                    role.setPermissions(item.getPermissions());
                }
                roleRepository.save(role);
            }
        }
    }

    private void removeById(final List<?> items, final String id) {
        if (items == null || id == null) {
            return;
        }
        items.removeIf(item -> id.equals(extractId(item)));
    }

    private String extractId(final Object item) {
        if (item instanceof SettingsProjectStatus) {
            return ((SettingsProjectStatus) item).getId();
        }
        if (item instanceof SettingsPriority) {
            return ((SettingsPriority) item).getId();
        }
        if (item instanceof SettingsTag) {
            return ((SettingsTag) item).getId();
        }
        if (item instanceof DocumentCategoryUpsertRequest) {
            return ((DocumentCategoryUpsertRequest) item).getId();
        }
        if (item instanceof ProjectTypeUpsertRequest) {
            return ((ProjectTypeUpsertRequest) item).getId();
        }
        if (item instanceof ContractTypeUpsertRequest) {
            return ((ContractTypeUpsertRequest) item).getId();
        }
        if (item instanceof ProjectStatusUpsertRequest) {
            return ((ProjectStatusUpsertRequest) item).getId();
        }
        if (item instanceof PriorityUpsertRequest) {
            return ((PriorityUpsertRequest) item).getId();
        }
        if (item instanceof TagUpsertRequest) {
            return ((TagUpsertRequest) item).getId();
        }
        if (item instanceof RoleUpsertRequest) {
            return ((RoleUpsertRequest) item).getId();
        }
        return null;
    }

    private boolean isTrue(final Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    private <T> List<T> ensureList(final List<T> items) {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    private <T> Map<String, T> indexById(final List<T> items) {
        if (items == null) {
            return Collections.emptyMap();
        }
        return items.stream()
                .filter(item -> extractId(item) != null)
                .collect(Collectors.toMap(this::extractId, Function.identity(), (a, b) -> a));
    }

    private List<ProjectStatusSettingsResponse> toProjectStatusResponses(final List<SettingsProjectStatus> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(item -> ProjectStatusSettingsResponse.builder()
                        .id(item.getId())
                        .key(item.getKey())
                        .name(item.getName())
                        .color(item.getColor())
                        .category(item.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    private List<PrioritySettingsResponse> toPriorityResponses(final List<SettingsPriority> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(item -> PrioritySettingsResponse.builder()
                        .id(item.getId())
                        .key(item.getKey())
                        .name(item.getName())
                        .color(item.getColor())
                        .build())
                .collect(Collectors.toList());
    }

    private List<TagSettingsResponse> toTagResponses(final List<SettingsTag> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(item -> TagSettingsResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .color(item.getColor())
                        .build())
                .collect(Collectors.toList());
    }

    private List<RoleSettingsResponse> toRoleResponses(final List<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(role -> RoleSettingsResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(role.getPermissions())
                        .build())
                .collect(Collectors.toList());
    }
}
