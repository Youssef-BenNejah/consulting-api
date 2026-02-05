package brama.consultant_business_api.domain.settings.notification.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class NotificationSettingsUpdateRequest {
    @NotNull
    private Boolean emailNotifications;
    @NotNull
    private Boolean slackIntegration;
    @NotNull
    private Boolean milestoneReminders;
    @NotNull
    private Boolean invoiceDueAlerts;
    @NotNull
    private Boolean weeklyDigest;
}
