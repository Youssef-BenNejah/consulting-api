package brama.consultant_business_api.domain.issue.dto.request;

import brama.consultant_business_api.domain.issue.enums.IssueSeverity;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
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
public class IssueCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String projectId;
    @NotBlank
    private String projectName;
    @NotNull
    private IssueSeverity severity;
    @NotBlank
    private String owner;
    @NotBlank
    private String mitigationPlan;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private IssueStatus status;
}
