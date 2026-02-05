package brama.consultant_business_api.domain.risk.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.risk.enums.RiskStatus;
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
@Document(collection = "risks")
public class Risk extends BaseDocument {
    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("probability")
    private Double probability;

    @Field("impact")
    private Double impact;

    @Field("score")
    private Double score;

    @Field("owner")
    private String owner;

    @Field("mitigation_plan")
    private String mitigationPlan;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("status")
    private RiskStatus status;
}
