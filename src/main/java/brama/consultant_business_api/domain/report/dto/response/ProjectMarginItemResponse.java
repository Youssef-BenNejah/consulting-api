package brama.consultant_business_api.domain.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMarginItemResponse {
    private String id;
    private String name;
    private String clientName;
    private double clientBudget;
    private double vendorCost;
    private double internalCost;
    private double margin;
    private double marginPercent;
}
