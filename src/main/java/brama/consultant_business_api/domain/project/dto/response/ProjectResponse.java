package brama.consultant_business_api.domain.project.dto.response;

import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private ProjectStatus status;
    private ProjectType type;
    private Double clientBudget;
    private Double vendorCost;
    private Double internalCost;
    private HealthStatus healthStatus;
    private Integer progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
