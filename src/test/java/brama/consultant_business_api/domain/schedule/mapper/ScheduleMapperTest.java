package brama.consultant_business_api.domain.schedule.mapper;

import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;
import brama.consultant_business_api.domain.schedule.model.ProjectSchedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleMapperTest {
    private final ScheduleMapper mapper = new ScheduleMapper();

    @Test
    void toEntityComputesDuration() {
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .projectId("p1")
                .projectName("Project")
                .clientId("c1")
                .clientName("Client")
                .scheduleStartDate(LocalDate.of(2026, 2, 1))
                .scheduleEndDate(LocalDate.of(2026, 2, 3))
                .scheduleColor("#000")
                .isScheduled(true)
                .reminderEnabled(false)
                .build();

        ProjectSchedule schedule = mapper.toEntity(request);
        assertThat(schedule.getScheduleDurationDays()).isEqualTo(3);
    }

    @Test
    void mergeUpdatesDuration() {
        ProjectSchedule schedule = ProjectSchedule.builder()
                .scheduleStartDate(LocalDate.of(2026, 2, 1))
                .scheduleEndDate(LocalDate.of(2026, 2, 1))
                .scheduleDurationDays(1)
                .build();

        mapper.merge(schedule, ScheduleUpdateRequest.builder()
                .scheduleEndDate(LocalDate.of(2026, 2, 2))
                .build());

        assertThat(schedule.getScheduleDurationDays()).isEqualTo(2);
    }
}

