package brama.consultant_business_api.domain.opportunity.dto.request;

import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class OpportunityUpdateRequest {
    private String clientId;
    private String clientName;
    private String title;
    @PositiveOrZero
    private Double expectedValue;
    @Min(0)
    @Max(100)
    private Double probability;
    private OpportunityStage stage;
    private LocalDate expectedCloseDate;
    private String notes;
}
