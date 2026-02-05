package brama.consultant_business_api.domain.milestone.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
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
@Document(collection = "milestones")
public class Milestone extends BaseDocument {
    @Field("name")
    private String name;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("deliverable")
    private String deliverable;

    @Field("acceptance_criteria")
    private String acceptanceCriteria;

    @Field("status")
    private MilestoneStatus status;

    @Field("sign_off_by")
    private String signOffBy;
}
