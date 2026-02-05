package brama.consultant_business_api.domain.settings.dto.request;

import brama.consultant_business_api.domain.settings.dto.request.items.ContractTypeUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.DocumentCategoryUpsertRequest;
import brama.consultant_business_api.domain.settings.dto.request.items.ProjectTypeUpsertRequest;
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

    @Valid
    private List<ProjectTypeUpsertRequest> projectTypes;
    private List<String> projectTypeDeletes;

    @Valid
    private List<ContractTypeUpsertRequest> contractTypes;
    private List<String> contractTypeDeletes;

    @Valid
    private GeneralSettingsPatchRequest general;

    @Valid
    private NotificationSettingsPatchRequest notifications;
}
