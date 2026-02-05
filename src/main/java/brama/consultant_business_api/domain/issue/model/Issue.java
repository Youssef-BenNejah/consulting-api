package brama.consultant_business_api.domain.issue.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.issue.enums.IssueSeverity;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;
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
@Document(collection = "issues")
public class Issue extends BaseDocument {
    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("severity")
    private IssueSeverity severity;

    @Field("owner")
    private String owner;

    @Field("mitigation_plan")
    private String mitigationPlan;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("status")
    private IssueStatus status;

    @Field("created_at")
    private LocalDate createdAt;
}
