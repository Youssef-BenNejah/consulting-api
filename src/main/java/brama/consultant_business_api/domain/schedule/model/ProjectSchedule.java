package brama.consultant_business_api.domain.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "project_schedules")
public class ProjectSchedule {
    @Id
    private String id;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("client_id")
    private String clientId;

    @Field("client_name")
    private String clientName;

    @Field("client_contact_name")
    private String clientContactName;

    @Field("client_contact_email")
    private String clientContactEmail;

    @Field("client_contact_phone")
    private String clientContactPhone;

    @Field("schedule_start_date")
    private LocalDate scheduleStartDate;

    @Field("schedule_end_date")
    private LocalDate scheduleEndDate;

    @Field("schedule_duration_days")
    private Integer scheduleDurationDays;

    @Field("schedule_color")
    private String scheduleColor;

    @Field("schedule_notes")
    private String scheduleNotes;

    @Field("is_scheduled")
    private boolean isScheduled;

    @Field("reminder_enabled")
    private boolean reminderEnabled;

    @Field("created_by")
    private String createdBy;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;
}
