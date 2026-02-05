package brama.consultant_business_api.service.settings.impl;

import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeUpdateRequest;
import brama.consultant_business_api.domain.contracttype.dto.response.ContractTypeResponse;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryUpdateRequest;
import brama.consultant_business_api.domain.documentcategory.dto.response.DocumentCategoryResponse;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeUpdateRequest;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;
import brama.consultant_business_api.domain.settings.dto.request.SettingsPatchRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ContractTypeUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.DocumentCategoryUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ProjectTypeUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.response.SettingsResponse;
import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsPatchRequest;
import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.general.dto.response.GeneralSettingsResponse;
import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsPatchRequest;
import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.notification.dto.response.NotificationSettingsResponse;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.ErrorCode;
import brama.consultant_business_api.service.contracttype.ContractTypeService;
import brama.consultant_business_api.service.documentcategory.DocumentCategoryService;
import brama.consultant_business_api.service.projecttype.ProjectTypeService;
import brama.consultant_business_api.service.settings.GeneralSettingsService;
import brama.consultant_business_api.service.settings.NotificationSettingsService;
import brama.consultant_business_api.service.settings.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
    private final DocumentCategoryService documentCategoryService;
    private final ProjectTypeService projectTypeService;
    private final ContractTypeService contractTypeService;
    private final GeneralSettingsService generalSettingsService;
    private final NotificationSettingsService notificationSettingsService;

    @Override
    public SettingsResponse getAll() {
        return SettingsResponse.builder()
                .documentCategories(documentCategoryService.list())
                .projectTypes(projectTypeService.list())
                .contractTypes(contractTypeService.list())
                .general(generalSettingsService.get())
                .notifications(notificationSettingsService.get())
                .build();
    }

    @Override
    public SettingsResponse patch(final SettingsPatchRequest request) {
        if (request == null) {
            return getAll();
        }

        deleteIfPresent(request.getDocumentCategoryDeletes(), documentCategoryService::delete);
        deleteIfPresent(request.getProjectTypeDeletes(), projectTypeService::delete);
        deleteIfPresent(request.getContractTypeDeletes(), contractTypeService::delete);

        if (request.getDocumentCategories() != null) {
            upsertDocumentCategories(request.getDocumentCategories());
        }
        if (request.getProjectTypes() != null) {
            upsertProjectTypes(request.getProjectTypes());
        }
        if (request.getContractTypes() != null) {
            upsertContractTypes(request.getContractTypes());
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
                requireCreateFields(item.getName(), item.getKey(), item.getColor(), "documentCategories");
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
                documentCategoryService.update(item.getId(), update);
            }
        }
    }

    private void upsertProjectTypes(final List<ProjectTypeUpsertRequest> items) {
        for (ProjectTypeUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                requireCreateFields(item.getName(), item.getKey(), item.getDescription(), "projectTypes");
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
                projectTypeService.update(item.getId(), update);
            }
        }
    }

    private void upsertContractTypes(final List<ContractTypeUpsertRequest> items) {
        for (ContractTypeUpsertRequest item : items) {
            if (isBlank(item.getId())) {
                requireCreateFields(item.getName(), item.getKey(), item.getDescription(), "contractTypes");
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
                contractTypeService.update(item.getId(), update);
            }
        }
    }

    private GeneralSettingsUpdateRequest toGeneralUpdate(final GeneralSettingsPatchRequest request) {
        if (isBlank(request.getCompanyName())
                || isBlank(request.getEmail())
                || isBlank(request.getTimezone())
                || isBlank(request.getDefaultCurrency())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,
                    "general settings update requires companyName, email, timezone, defaultCurrency");
        }
        return GeneralSettingsUpdateRequest.builder()
                .companyName(request.getCompanyName())
                .email(request.getEmail())
                .timezone(request.getTimezone())
                .defaultCurrency(request.getDefaultCurrency())
                .build();
    }

    private NotificationSettingsUpdateRequest toNotificationUpdate(final NotificationSettingsPatchRequest request) {
        if (request.getEmailNotifications() == null
                || request.getSlackIntegration() == null
                || request.getMilestoneReminders() == null
                || request.getInvoiceDueAlerts() == null
                || request.getWeeklyDigest() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,
                    "notifications update requires all notification fields");
        }
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

    private void requireCreateFields(final String name, final String key, final String third, final String section) {
        if (isBlank(name) || isBlank(key) || isBlank(third)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST,
                    "%s create requires name, key, and required field", section);
        }
    }

    private boolean isBlank(final String value) {
        return value == null || value.trim().isEmpty();
    }
}
