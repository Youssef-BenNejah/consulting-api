package brama.consultant_business_api.domain.project.dto.request;

import brama.consultant_business_api.domain.project.enums.HealthStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProjectCreateRequest {
    @NotBlank
    private String projectId;
    @NotBlank
    private String clientId;
    @NotBlank
    private String clientName;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotBlank
    private String statusId;
    @NotBlank
    private String projectTypeId;
    private String priorityId;
    private List<String> tagIds;
    private String contractTypeId;
    @NotNull
    @PositiveOrZero
    private Double clientBudget;
    @NotNull
    @PositiveOrZero
    private Double vendorCost;
    @NotNull
    @PositiveOrZero
    private Double internalCost;
    @NotNull
    private HealthStatus healthStatus;
    @NotNull
    @Min(0)
    @Max(100)
    private Integer progress;
}
