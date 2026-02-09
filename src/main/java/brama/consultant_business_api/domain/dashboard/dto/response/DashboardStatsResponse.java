package brama.consultant_business_api.domain.dashboard.dto.response;

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
public class DashboardStatsResponse {
    private long totalClients;
    private long activeProjects;
    private ProjectsByHealthResponse projectsByHealth;
    private FinancialsResponse financials;
    private PipelineResponse pipeline;
    private long tasksOverdue;
    private long milestonesOverdue;
    private long openIssues;
    private long criticalRisks;
}
