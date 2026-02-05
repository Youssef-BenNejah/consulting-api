package brama.consultant_business_api.domain.report.dto.response;

import brama.consultant_business_api.domain.project.dto.response.ProjectResponse;
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
public class ProjectsReportResponse {
    private ProjectStatusCountsResponse statusCounts;
    private List<ProjectResponse> projects;
}
