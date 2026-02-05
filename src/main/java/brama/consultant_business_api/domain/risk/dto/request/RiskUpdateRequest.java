package brama.consultant_business_api.domain.risk.dto.request;

import brama.consultant_business_api.domain.risk.enums.RiskStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class RiskUpdateRequest {
    private String title;
    private String description;
    private String projectId;
    private String projectName;
    @Min(0)
    @Max(100)
    private Double probability;
    @Min(0)
    @Max(100)
    private Double impact;
    private String owner;
    private String mitigationPlan;
    private LocalDate dueDate;
    private RiskStatus status;
}
