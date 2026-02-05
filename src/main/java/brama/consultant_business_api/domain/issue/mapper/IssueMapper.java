package brama.consultant_business_api.domain.issue.mapper;

import brama.consultant_business_api.domain.issue.dto.request.IssueCreateRequest;
import brama.consultant_business_api.domain.issue.dto.request.IssueUpdateRequest;
import brama.consultant_business_api.domain.issue.dto.response.IssueResponse;
import brama.consultant_business_api.domain.issue.model.Issue;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class IssueMapper {
    public Issue toEntity(final IssueCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Issue.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .severity(request.getSeverity())
                .owner(request.getOwner())
                .mitigationPlan(request.getMitigationPlan())
                .dueDate(request.getDueDate())
                .status(request.getStatus())
                .createdAt(LocalDate.now())
                .build();
    }

    public void merge(final Issue issue, final IssueUpdateRequest request) {
        if (issue == null || request == null) {
            return;
        }
        if (request.getTitle() != null) {
            issue.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            issue.setDescription(request.getDescription());
        }
        if (request.getProjectId() != null) {
            issue.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            issue.setProjectName(request.getProjectName());
        }
        if (request.getSeverity() != null) {
            issue.setSeverity(request.getSeverity());
        }
        if (request.getOwner() != null) {
            issue.setOwner(request.getOwner());
        }
        if (request.getMitigationPlan() != null) {
            issue.setMitigationPlan(request.getMitigationPlan());
        }
        if (request.getDueDate() != null) {
            issue.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null) {
            issue.setStatus(request.getStatus());
        }
    }

    public IssueResponse toResponse(final Issue issue) {
        if (issue == null) {
            return null;
        }
        return IssueResponse.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .projectId(issue.getProjectId())
                .projectName(issue.getProjectName())
                .severity(issue.getSeverity())
                .owner(issue.getOwner())
                .mitigationPlan(issue.getMitigationPlan())
                .dueDate(issue.getDueDate())
                .status(issue.getStatus())
                .createdAt(issue.getCreatedAt())
                .build();
    }
}
