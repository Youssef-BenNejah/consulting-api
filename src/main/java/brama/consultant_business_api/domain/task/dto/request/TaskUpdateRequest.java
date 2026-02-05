package brama.consultant_business_api.domain.task.dto.request;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.PositiveOrZero;
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
public class TaskUpdateRequest {
    private String title;
    private String description;
    private String projectId;
    private String projectName;
    private String owner;
    private OwnerType ownerType;
    private LocalDate dueDate;
    private TaskStatus status;
    private Priority priority;
    @PositiveOrZero
    private Double estimatedHours;
    @PositiveOrZero
    private Double actualHours;
    private String milestoneId;
}
