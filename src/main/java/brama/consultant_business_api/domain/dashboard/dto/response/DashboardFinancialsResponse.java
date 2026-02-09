package brama.consultant_business_api.domain.dashboard.dto.response;

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
public class DashboardFinancialsResponse {
    private RevenueSummaryResponse revenue;
    private CostsSummaryResponse costs;
    private double netMargin;
    private String currency;
}
