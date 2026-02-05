package brama.consultant_business_api.domain.task.dto.request;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TaskCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String projectId;
    @NotBlank
    private String projectName;
    @NotBlank
    private String owner;
    @NotNull
    private OwnerType ownerType;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private TaskStatus status;
    @NotNull
    private Priority priority;
    @NotNull
    @PositiveOrZero
    private Double estimatedHours;
    @NotNull
    @PositiveOrZero
    private Double actualHours;
    private String milestoneId;
}
