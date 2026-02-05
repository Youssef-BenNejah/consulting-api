package brama.consultant_business_api.domain.task.mapper;

import brama.consultant_business_api.domain.task.dto.request.TaskCreateRequest;
import brama.consultant_business_api.domain.task.dto.request.TaskUpdateRequest;
import brama.consultant_business_api.domain.task.dto.response.TaskResponse;
import brama.consultant_business_api.domain.task.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(final TaskCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .owner(request.getOwner())
                .ownerType(request.getOwnerType())
                .dueDate(request.getDueDate())
                .status(request.getStatus())
                .priority(request.getPriority())
                .estimatedHours(request.getEstimatedHours())
                .actualHours(request.getActualHours())
                .milestoneId(request.getMilestoneId())
                .build();
    }

    public void merge(final Task task, final TaskUpdateRequest request) {
        if (task == null || request == null) {
            return;
        }
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getProjectId() != null) {
            task.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            task.setProjectName(request.getProjectName());
        }
        if (request.getOwner() != null) {
            task.setOwner(request.getOwner());
        }
        if (request.getOwnerType() != null) {
            task.setOwnerType(request.getOwnerType());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getEstimatedHours() != null) {
            task.setEstimatedHours(request.getEstimatedHours());
        }
        if (request.getActualHours() != null) {
            task.setActualHours(request.getActualHours());
        }
        if (request.getMilestoneId() != null) {
            task.setMilestoneId(request.getMilestoneId());
        }
    }

    public TaskResponse toResponse(final Task task) {
        if (task == null) {
            return null;
        }
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .projectId(task.getProjectId())
                .projectName(task.getProjectName())
                .owner(task.getOwner())
                .ownerType(task.getOwnerType())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .priority(task.getPriority())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .milestoneId(task.getMilestoneId())
                .build();
    }
}
