package brama.consultant_business_api.domain.opportunity.dto.request;

import brama.consultant_business_api.domain.opportunity.enums.OpportunityStage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class OpportunityCreateRequest {
    @NotBlank
    private String clientId;
    @NotBlank
    private String clientName;
    @NotBlank
    private String title;
    @NotNull
    @PositiveOrZero
    private Double expectedValue;
    @NotNull
    @Min(0)
    @Max(100)
    private Double probability;
    @NotNull
    private OpportunityStage stage;
    @NotNull
    private LocalDate expectedCloseDate;
    @NotBlank
    private String notes;
}
