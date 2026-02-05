package brama.consultant_business_api.domain.issue.dto.response;

import brama.consultant_business_api.domain.issue.enums.IssueSeverity;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
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
public class IssueResponse {
    private String id;
    private String title;
    private String description;
    private String projectId;
    private String projectName;
    private IssueSeverity severity;
    private String owner;
    private String mitigationPlan;
    private LocalDate dueDate;
    private IssueStatus status;
    private LocalDate createdAt;
}
