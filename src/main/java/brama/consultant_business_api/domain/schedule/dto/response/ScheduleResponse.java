package brama.consultant_business_api.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private String id;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("project_name")
    private String projectName;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("client_contact_name")
    private String clientContactName;
    @JsonProperty("client_contact_email")
    private String clientContactEmail;
    @JsonProperty("client_contact_phone")
    private String clientContactPhone;
    @JsonProperty("schedule_start_date")
    private LocalDate scheduleStartDate;
    @JsonProperty("schedule_end_date")
    private LocalDate scheduleEndDate;
    @JsonProperty("schedule_duration_days")
    private Integer scheduleDurationDays;
    @JsonProperty("schedule_color")
    private String scheduleColor;
    @JsonProperty("schedule_notes")
    private String scheduleNotes;
    @JsonProperty("is_scheduled")
    private boolean isScheduled;
    @JsonProperty("reminder_enabled")
    private boolean reminderEnabled;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
