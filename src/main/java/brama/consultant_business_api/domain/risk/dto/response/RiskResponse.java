package brama.consultant_business_api.domain.risk.dto.response;

import brama.consultant_business_api.domain.risk.enums.RiskStatus;
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
public class RiskResponse {
    private String id;
    private String title;
    private String description;
    private String projectId;
    private String projectName;
    private Double probability;
    private Double impact;
    private Double score;
    private String owner;
    private String mitigationPlan;
    private LocalDate dueDate;
    private RiskStatus status;
}
