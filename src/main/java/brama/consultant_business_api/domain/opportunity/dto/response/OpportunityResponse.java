package brama.consultant_business_api.domain.opportunity.dto.response;

import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;
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
public class OpportunityResponse {
    private String id;
    private String clientId;
    private String clientName;
    private String title;
    private Double expectedValue;
    private Double probability;
    private OpportunityStage stage;
    private LocalDate expectedCloseDate;
    private String notes;
}
