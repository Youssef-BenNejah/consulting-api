package brama.consultant_business_api.domain.task.dto.response;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
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
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private String projectId;
    private String projectName;
    private String owner;
    private OwnerType ownerType;
    private LocalDate dueDate;
    private TaskStatus status;
    private Priority priority;
    private Double estimatedHours;
    private Double actualHours;
    private String milestoneId;
}
