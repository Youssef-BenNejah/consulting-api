package brama.consultant_business_api.domain.project.dto.response;

import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;
import brama.consultant_business_api.domain.contracttype.dto.response.ContractTypeResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.PrioritySettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.ProjectStatusSettingsResponse;
import brama.consultant_business_api.domain.settings.dto.response.items.TagSettingsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private String id;
    private String projectId;
    private String clientId;
    private String clientName;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String statusId;
    private String projectTypeId;
    private String priorityId;
    private List<String> tagIds;
    private String contractTypeId;
    private ProjectStatusSettingsResponse status;
    private ProjectTypeResponse projectType;
    private PrioritySettingsResponse priority;
    private List<TagSettingsResponse> tags;
    private ContractTypeResponse contractType;
    private Double clientBudget;
    private Double vendorCost;
    private Double internalCost;
    private HealthStatus healthStatus;
    private Integer progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
