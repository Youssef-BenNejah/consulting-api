package brama.consultant_business_api.domain.userstory.mapper;

import brama.consultant_business_api.domain.userstory.dto.request.UserStoryCreateRequest;
import brama.consultant_business_api.domain.userstory.dto.request.UserStoryUpdateRequest;
import brama.consultant_business_api.domain.userstory.dto.response.UserStoryResponse;
import brama.consultant_business_api.domain.userstory.model.UserStory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class UserStoryMapper {
    public UserStory toEntity(final UserStoryCreateRequest request) {
        if (request == null) {
            return null;
        }
        return UserStory.builder()
                .epicId(request.getEpicId())
                .epicTitle(request.getEpicTitle())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .title(request.getTitle())
                .description(request.getDescription())
                .acceptanceCriteria(request.getAcceptanceCriteria() != null ? new ArrayList<>(request.getAcceptanceCriteria()) : null)
                .priority(request.getPriority())
                .status(request.getStatus())
                .effort(request.getEffort())
                .notes(request.getNotes())
                .createdAt(LocalDate.now())
                .build();
    }

    public void merge(final UserStory story, final UserStoryUpdateRequest request) {
        if (story == null || request == null) {
            return;
        }
        if (request.getEpicId() != null) {
            story.setEpicId(request.getEpicId());
        }
        if (request.getEpicTitle() != null) {
            story.setEpicTitle(request.getEpicTitle());
        }
        if (request.getProjectId() != null) {
            story.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            story.setProjectName(request.getProjectName());
        }
        if (request.getTitle() != null) {
            story.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            story.setDescription(request.getDescription());
        }
        if (request.getAcceptanceCriteria() != null) {
            story.setAcceptanceCriteria(new ArrayList<>(request.getAcceptanceCriteria()));
        }
        if (request.getPriority() != null) {
            story.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            story.setStatus(request.getStatus());
        }
        if (request.getEffort() != null) {
            story.setEffort(request.getEffort());
        }
        if (request.getNotes() != null) {
            story.setNotes(request.getNotes());
        }
    }

    public UserStoryResponse toResponse(final UserStory story) {
        if (story == null) {
            return null;
        }
        return UserStoryResponse.builder()
                .id(story.getId())
                .epicId(story.getEpicId())
                .epicTitle(story.getEpicTitle())
                .projectId(story.getProjectId())
                .projectName(story.getProjectName())
                .title(story.getTitle())
                .description(story.getDescription())
                .acceptanceCriteria(story.getAcceptanceCriteria())
                .priority(story.getPriority())
                .status(story.getStatus())
                .effort(story.getEffort())
                .notes(story.getNotes())
                .createdAt(story.getCreatedAt())
                .build();
    }
}
