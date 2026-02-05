package brama.consultant_business_api.domain.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarginsReportResponse {
    private double totalRevenue;
    private double totalCost;
    private double totalMargin;
    private double avgMarginPercent;
    private List<ProjectMarginItemResponse> projects;
}
