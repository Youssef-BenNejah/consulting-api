package brama.consultant_business_api.domain.settings.notification.dto.response;

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
public class NotificationSettingsResponse {
    private Boolean emailNotifications;
    private Boolean slackIntegration;
    private Boolean milestoneReminders;
    private Boolean invoiceDueAlerts;
    private Boolean weeklyDigest;
}
