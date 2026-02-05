package brama.consultant_business_api.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ScheduleUpdateRequest {
    @JsonProperty("project_id")
    @JsonAlias("projectId")
    private String projectId;
    @JsonProperty("project_name")
    @JsonAlias("projectName")
    private String projectName;
    @JsonProperty("client_id")
    @JsonAlias("clientId")
    private String clientId;
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
    @JsonProperty("schedule_start_date")
    @JsonAlias("scheduleStartDate")
    private LocalDate scheduleStartDate;
    @JsonProperty("schedule_end_date")
    @JsonAlias("scheduleEndDate")
    private LocalDate scheduleEndDate;
    @JsonProperty("schedule_color")
    @JsonAlias("scheduleColor")
    private String scheduleColor;
    @JsonProperty("schedule_notes")
    @JsonAlias("scheduleNotes")
    private String scheduleNotes;
    @JsonProperty("is_scheduled")
    @JsonAlias("isScheduled")
    private Boolean isScheduled;
    @JsonProperty("reminder_enabled")
    @JsonAlias("reminderEnabled")
    private Boolean reminderEnabled;
    @JsonProperty("created_by")
    @JsonAlias("createdBy")
    private String createdBy;
}
