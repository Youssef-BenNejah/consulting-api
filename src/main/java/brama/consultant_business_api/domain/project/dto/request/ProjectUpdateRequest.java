package brama.consultant_business_api.domain.project.dto.request;

import brama.consultant_business_api.domain.project.enums.HealthStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequest {
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
    @PositiveOrZero
    private Double clientBudget;
    @PositiveOrZero
    private Double vendorCost;
    @PositiveOrZero
    private Double internalCost;
    private HealthStatus healthStatus;
    @Min(0)
    @Max(100)
    private Integer progress;
}
