package brama.consultant_business_api.domain.settings.dto.response;

import brama.consultant_business_api.domain.contracttype.dto.response.ContractTypeResponse;
import brama.consultant_business_api.domain.documentcategory.dto.response.DocumentCategoryResponse;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;
import brama.consultant_business_api.domain.settings.general.dto.response.GeneralSettingsResponse;
import brama.consultant_business_api.domain.settings.notification.dto.response.NotificationSettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.ProjectStatusSettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.PrioritySettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.RoleSettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.TagSettingsResponse;
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
public class SettingsResponse {
    private List<DocumentCategoryResponse> documentCategories;
    private List<ProjectTypeResponse> projectTypes;
    private List<ContractTypeResponse> contractTypes;
    private List<ProjectStatusSettingsResponse> projectStatuses;
    private List<PrioritySettingsResponse> priorities;
    private List<TagSettingsResponse> tags;
    private List<RoleSettingsResponse> roles;
    private GeneralSettingsResponse general;
    private NotificationSettingsResponse notifications;
}
