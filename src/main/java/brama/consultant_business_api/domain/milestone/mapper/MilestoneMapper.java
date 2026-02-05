package brama.consultant_business_api.domain.milestone.mapper;

import brama.consultant_business_api.domain.milestone.dto.request.MilestoneCreateRequest;
import brama.consultant_business_api.domain.milestone.dto.request.MilestoneUpdateRequest;
import brama.consultant_business_api.domain.milestone.dto.response.MilestoneResponse;
import brama.consultant_business_api.domain.milestone.model.Milestone;
import org.springframework.stereotype.Component;

@Component
public class MilestoneMapper {
    public Milestone toEntity(final MilestoneCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Milestone.builder()
                .name(request.getName())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .dueDate(request.getDueDate())
                .deliverable(request.getDeliverable())
                .acceptanceCriteria(request.getAcceptanceCriteria())
                .status(request.getStatus())
                .signOffBy(request.getSignOffBy())
                .build();
    }

    public void merge(final Milestone milestone, final MilestoneUpdateRequest request) {
        if (milestone == null || request == null) {
            return;
        }
        if (request.getName() != null) {
            milestone.setName(request.getName());
        }
        if (request.getProjectId() != null) {
            milestone.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            milestone.setProjectName(request.getProjectName());
        }
        if (request.getDueDate() != null) {
            milestone.setDueDate(request.getDueDate());
        }
        if (request.getDeliverable() != null) {
            milestone.setDeliverable(request.getDeliverable());
        }
        if (request.getAcceptanceCriteria() != null) {
            milestone.setAcceptanceCriteria(request.getAcceptanceCriteria());
        }
        if (request.getStatus() != null) {
            milestone.setStatus(request.getStatus());
        }
        if (request.getSignOffBy() != null) {
            milestone.setSignOffBy(request.getSignOffBy());
        }
    }

    public MilestoneResponse toResponse(final Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        return MilestoneResponse.builder()
                .id(milestone.getId())
                .name(milestone.getName())
                .projectId(milestone.getProjectId())
                .projectName(milestone.getProjectName())
                .dueDate(milestone.getDueDate())
                .deliverable(milestone.getDeliverable())
                .acceptanceCriteria(milestone.getAcceptanceCriteria())
                .status(milestone.getStatus())
                .signOffBy(milestone.getSignOffBy())
                .build();
    }
}
