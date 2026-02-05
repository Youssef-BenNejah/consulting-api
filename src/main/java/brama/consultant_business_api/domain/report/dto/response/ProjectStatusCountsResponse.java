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
public class ProjectStatusCountsResponse {
    private long delivery;
    private long discovery;
    private long review;
    private long closed;
    private long other;
}
