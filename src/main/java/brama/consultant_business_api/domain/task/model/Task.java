package brama.consultant_business_api.domain.task.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.task.enums.OwnerType;
import brama.consultant_business_api.domain.task.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task extends BaseDocument {
    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("owner")
    private String owner;

    @Field("owner_type")
    private OwnerType ownerType;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("status")
    private TaskStatus status;

    @Field("priority")
    private Priority priority;

    @Field("estimated_hours")
    private Double estimatedHours;

    @Field("actual_hours")
    private Double actualHours;

    @Field("milestone_id")
    private String milestoneId;
}
