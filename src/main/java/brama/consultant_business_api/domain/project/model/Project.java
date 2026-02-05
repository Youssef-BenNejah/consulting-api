package brama.consultant_business_api.domain.project.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.enums.ProjectType;
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
@Document(collection = "projects")
public class Project extends BaseDocument {
    @Field("project_id")
    private String projectId;

    @Field("client_id")
    private String clientId;

    @Field("client_name")
    private String clientName;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("status")
    private ProjectStatus status;

    @Field("type")
    private ProjectType type;

    @Field("client_budget")
    private Double clientBudget;

    @Field("vendor_cost")
    private Double vendorCost;

    @Field("internal_cost")
    private Double internalCost;

    @Field("health_status")
    private HealthStatus healthStatus;

    @Field("progress")
    private Integer progress;

    @Field("open_tasks")
    private Integer openTasks;

    @Field("open_issues")
    private Integer openIssues;

    @Field("open_risks")
    private Integer openRisks;
}
