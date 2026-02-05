package brama.consultant_business_api.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    @JsonAlias("projectId")
    private String projectId;
    @NotBlank
    @JsonProperty("project_name")
    @JsonAlias("projectName")
    private String projectName;
    @NotBlank
    @JsonProperty("client_id")
    @JsonAlias("clientId")
    private String clientId;
    @NotBlank
    @JsonProperty("client_name")
    @JsonAlias("clientName")
    private String clientName;
    @JsonProperty("client_contact_name")
    @JsonAlias("clientContactName")
    private String clientContactName;
    @JsonProperty("client_contact_email")
    @JsonAlias("clientContactEmail")
    private String clientContactEmail;
    @JsonProperty("client_contact_phone")
    @JsonAlias("clientContactPhone")
    private String clientContactPhone;
    @NotNull
    @JsonProperty("schedule_start_date")
    @JsonAlias("scheduleStartDate")
    private LocalDate scheduleStartDate;
    @NotNull
    @JsonProperty("schedule_end_date")
    @JsonAlias("scheduleEndDate")
    private LocalDate scheduleEndDate;
    @NotBlank
    @JsonProperty("schedule_color")
    @JsonAlias("scheduleColor")
    private String scheduleColor;
    @JsonProperty("schedule_notes")
    @JsonAlias("scheduleNotes")
    private String scheduleNotes;
    @NotNull
    @JsonProperty("is_scheduled")
    @JsonAlias("isScheduled")
    private Boolean isScheduled;
    @NotNull
    @JsonProperty("reminder_enabled")
    @JsonAlias("reminderEnabled")
    private Boolean reminderEnabled;
    @JsonProperty("created_by")
    @JsonAlias("createdBy")
    private String createdBy;
}
