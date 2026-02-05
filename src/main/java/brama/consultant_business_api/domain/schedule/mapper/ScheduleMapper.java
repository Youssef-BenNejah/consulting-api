package brama.consultant_business_api.domain.schedule.mapper;

import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;
import brama.consultant_business_api.domain.schedule.dto.response.ScheduleResponse;
import brama.consultant_business_api.domain.schedule.model.ProjectSchedule;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ScheduleMapper {
    public ProjectSchedule toEntity(final ScheduleCreateRequest request) {
        if (request == null) {
            return null;
        }
        final Instant now = Instant.now();
        return ProjectSchedule.builder()
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .clientContactName(request.getClientContactName())
                .clientContactEmail(request.getClientContactEmail())
                .clientContactPhone(request.getClientContactPhone())
                .scheduleStartDate(request.getScheduleStartDate())
                .scheduleEndDate(request.getScheduleEndDate())
                .scheduleDurationDays(computeDurationDays(request.getScheduleStartDate(), request.getScheduleEndDate()))
                .scheduleColor(request.getScheduleColor())
                .scheduleNotes(request.getScheduleNotes())
                .isScheduled(Boolean.TRUE.equals(request.getIsScheduled()))
                .reminderEnabled(Boolean.TRUE.equals(request.getReminderEnabled()))
                .createdBy(request.getCreatedBy())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void merge(final ProjectSchedule schedule, final ScheduleUpdateRequest request) {
        if (schedule == null || request == null) {
            return;
        }
        if (request.getProjectId() != null) {
            schedule.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            schedule.setProjectName(request.getProjectName());
        }
        if (request.getClientId() != null) {
            schedule.setClientId(request.getClientId());
        }
        if (request.getClientName() != null) {
            schedule.setClientName(request.getClientName());
        }
        if (request.getClientContactName() != null) {
            schedule.setClientContactName(request.getClientContactName());
        }
        if (request.getClientContactEmail() != null) {
            schedule.setClientContactEmail(request.getClientContactEmail());
        }
        if (request.getClientContactPhone() != null) {
            schedule.setClientContactPhone(request.getClientContactPhone());
        }
        if (request.getScheduleStartDate() != null) {
            schedule.setScheduleStartDate(request.getScheduleStartDate());
        }
        if (request.getScheduleEndDate() != null) {
            schedule.setScheduleEndDate(request.getScheduleEndDate());
        }
        if (request.getScheduleStartDate() != null || request.getScheduleEndDate() != null) {
            schedule.setScheduleDurationDays(computeDurationDays(schedule.getScheduleStartDate(), schedule.getScheduleEndDate()));
        }
        if (request.getScheduleColor() != null) {
            schedule.setScheduleColor(request.getScheduleColor());
        }
        if (request.getScheduleNotes() != null) {
            schedule.setScheduleNotes(request.getScheduleNotes());
        }
        if (request.getIsScheduled() != null) {
            schedule.setScheduled(request.getIsScheduled());
        }
        if (request.getReminderEnabled() != null) {
            schedule.setReminderEnabled(request.getReminderEnabled());
        }
        if (request.getCreatedBy() != null) {
            schedule.setCreatedBy(request.getCreatedBy());
        }
        schedule.setUpdatedAt(Instant.now());
    }

    public ScheduleResponse toResponse(final ProjectSchedule schedule) {
        if (schedule == null) {
            return null;
        }
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .projectId(schedule.getProjectId())
                .projectName(schedule.getProjectName())
                .clientId(schedule.getClientId())
                .clientName(schedule.getClientName())
                .clientContactName(schedule.getClientContactName())
                .clientContactEmail(schedule.getClientContactEmail())
                .clientContactPhone(schedule.getClientContactPhone())
                .scheduleStartDate(schedule.getScheduleStartDate())
                .scheduleEndDate(schedule.getScheduleEndDate())
                .scheduleDurationDays(schedule.getScheduleDurationDays())
                .scheduleColor(schedule.getScheduleColor())
                .scheduleNotes(schedule.getScheduleNotes())
                .isScheduled(schedule.isScheduled())
                .reminderEnabled(schedule.isReminderEnabled())
                .createdBy(schedule.getCreatedBy())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    private Integer computeDurationDays(final LocalDate start, final LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }
}
