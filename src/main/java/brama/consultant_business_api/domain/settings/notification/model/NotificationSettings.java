package brama.consultant_business_api.domain.settings.notification.model;

import brama.consultant_business_api.common.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "settings_notifications")
public class NotificationSettings extends BaseDocument {
    @Field("email_notifications")
    private Boolean emailNotifications;

    @Field("slack_integration")
    private Boolean slackIntegration;

    @Field("milestone_reminders")
    private Boolean milestoneReminders;

    @Field("invoice_due_alerts")
    private Boolean invoiceDueAlerts;

    @Field("weekly_digest")
    private Boolean weeklyDigest;
}
