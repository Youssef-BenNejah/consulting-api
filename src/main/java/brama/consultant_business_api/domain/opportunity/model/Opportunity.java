package brama.consultant_business_api.domain.opportunity.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;
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
@Document(collection = "opportunities")
public class Opportunity extends BaseDocument {
    @Field("client_id")
    private String clientId;

    @Field("client_name")
    private String clientName;

    @Field("title")
    private String title;

    @Field("expected_value")
    private Double expectedValue;

    @Field("probability")
    private Double probability;

    @Field("stage")
    private OpportunityStage stage;

    @Field("expected_close_date")
    private LocalDate expectedCloseDate;

    @Field("notes")
    private String notes;
}
