package brama.consultant_business_api.domain.epic.mapper;

import brama.consultant_business_api.domain.epic.dto.request.EpicCreateRequest;
import brama.consultant_business_api.domain.epic.dto.request.EpicUpdateRequest;
import brama.consultant_business_api.domain.epic.dto.response.EpicResponse;
import brama.consultant_business_api.domain.epic.model.Epic;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EpicMapper {
    public Epic toEntity(final EpicCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Epic.builder()
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(request.getStatus())
                .progress(request.getProgress())
                .storyCount(request.getStoryCount())
                .createdAt(LocalDate.now())
                .build();
    }

    public void merge(final Epic epic, final EpicUpdateRequest request) {
        if (epic == null || request == null) {
            return;
        }
        if (request.getProjectId() != null) {
            epic.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            epic.setProjectName(request.getProjectName());
        }
        if (request.getTitle() != null) {
            epic.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            epic.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            epic.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            epic.setStatus(request.getStatus());
        }
        if (request.getProgress() != null) {
            epic.setProgress(request.getProgress());
        }
        if (request.getStoryCount() != null) {
            epic.setStoryCount(request.getStoryCount());
        }
    }

    public EpicResponse toResponse(final Epic epic) {
        if (epic == null) {
            return null;
        }
        return EpicResponse.builder()
                .id(epic.getId())
                .projectId(epic.getProjectId())
                .projectName(epic.getProjectName())
                .title(epic.getTitle())
                .description(epic.getDescription())
                .priority(epic.getPriority())
                .status(epic.getStatus())
                .progress(epic.getProgress())
                .storyCount(epic.getStoryCount())
                .createdAt(epic.getCreatedAt())
                .build();
    }
}
