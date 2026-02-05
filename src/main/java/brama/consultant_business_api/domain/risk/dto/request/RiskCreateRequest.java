package brama.consultant_business_api.domain.risk.dto.request;

import brama.consultant_business_api.domain.risk.enums.RiskStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String projectId;
    @NotBlank
    private String projectName;
    @NotNull
    @Min(0)
    @Max(100)
    private Double probability;
    @NotNull
    @Min(0)
    @Max(100)
    private Double impact;
    @NotBlank
    private String owner;
    @NotBlank
    private String mitigationPlan;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private RiskStatus status;
}
