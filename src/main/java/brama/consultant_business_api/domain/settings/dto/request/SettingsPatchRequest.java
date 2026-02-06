package brama.consultant_business_api.domain.settings.dto.request;

import brama.consultant_business_api.domain.settings.dto.request.items.ContractTypeUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.DocumentCategoryUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ProjectTypeUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ProjectStatusUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.PriorityUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.RoleUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.TagUpsertRequest;
import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsPatchRequest;
import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsPatchRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsPatchRequest {
    @Valid
    private List<DocumentCategoryUpsertRequest> documentCategories;
    private List<String> documentCategoryDeletes;
    private Boolean replaceDocumentCategories;

    @Valid
    private List<ProjectTypeUpsertRequest> projectTypes;
    private List<String> projectTypeDeletes;
    private Boolean replaceProjectTypes;

    @Valid
    private List<ContractTypeUpsertRequest> contractTypes;
    private List<String> contractTypeDeletes;
    private Boolean replaceContractTypes;

    @Valid
    private List<ProjectStatusUpsertRequest> projectStatuses;
    private List<String> projectStatusDeletes;
    private Boolean replaceProjectStatuses;

    @Valid
    private List<PriorityUpsertRequest> priorities;
    private List<String> priorityDeletes;
    private Boolean replacePriorities;

    @Valid
    private List<TagUpsertRequest> tags;
    private List<String> tagDeletes;
    private Boolean replaceTags;

    @Valid
    private List<RoleUpsertRequest> roles;
    private List<String> roleDeletes;
    private Boolean replaceRoles;

    @Valid
    private GeneralSettingsPatchRequest general;

    @Valid
    private NotificationSettingsPatchRequest notifications;
}
