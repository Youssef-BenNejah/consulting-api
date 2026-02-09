package brama.consultant_business_api.domain.dashboard.dto.response;

import brama.consultant_business_api.domain.project.enums.HealthStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriticalProjectResponse {
    private String id;
    private String name;
    private String clientName;
    private HealthStatus healthStatus;
    private Integer progress;
    private long openIssues;
}
