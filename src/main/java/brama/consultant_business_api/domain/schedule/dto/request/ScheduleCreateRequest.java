package brama.consultant_business_api.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ScheduleCreateRequest {
    @NotBlank
    @JsonProperty("project_id")
    private String projectId;
    @NotBlank
    @JsonProperty("project_name")
    private String projectName;
    @NotBlank
    @JsonProperty("client_id")
    private String clientId;
    @NotBlank
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("client_contact_name")
    private String clientContactName;
    @JsonProperty("client_contact_email")
    private String clientContactEmail;
    @JsonProperty("client_contact_phone")
    private String clientContactPhone;
    @NotNull
    @JsonProperty("schedule_start_date")
    private LocalDate scheduleStartDate;
    @NotNull
    @JsonProperty("schedule_end_date")
    private LocalDate scheduleEndDate;
    @NotBlank
    @JsonProperty("schedule_color")
    private String scheduleColor;
    @JsonProperty("schedule_notes")
    private String scheduleNotes;
    @NotNull
    @JsonProperty("is_scheduled")
    private Boolean isScheduled;
    @NotNull
    @JsonProperty("reminder_enabled")
    private Boolean reminderEnabled;
    @JsonProperty("created_by")
    private String createdBy;
}
