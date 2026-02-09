package brama.consultant_business_api.domain.dashboard.dto.response;

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
public class UpcomingScheduleItemResponse {
    private String id;
    private String projectName;
    private String clientName;
    private LocalDate scheduleStartDate;
    private LocalDate scheduleEndDate;
    private String scheduleColor;
    private long daysUntilStart;
}
