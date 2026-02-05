package brama.consultant_business_api.domain.settings.notification.mapper;

import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.notification.dto.response.NotificationSettingsResponse;
import brama.consultant_business_api.domain.settings.notification.model.NotificationSettings;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingsMapper {
    public NotificationSettings toEntity(final NotificationSettingsUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return NotificationSettings.builder()
                .emailNotifications(request.getEmailNotifications())
                .slackIntegration(request.getSlackIntegration())
                .milestoneReminders(request.getMilestoneReminders())
                .invoiceDueAlerts(request.getInvoiceDueAlerts())
                .weeklyDigest(request.getWeeklyDigest())
                .build();
    }

    public void merge(final NotificationSettings settings, final NotificationSettingsUpdateRequest request) {
        if (settings == null || request == null) {
            return;
        }
        settings.setEmailNotifications(request.getEmailNotifications());
        settings.setSlackIntegration(request.getSlackIntegration());
        settings.setMilestoneReminders(request.getMilestoneReminders());
        settings.setInvoiceDueAlerts(request.getInvoiceDueAlerts());
        settings.setWeeklyDigest(request.getWeeklyDigest());
    }

    public NotificationSettingsResponse toResponse(final NotificationSettings settings) {
        if (settings == null) {
            return null;
        }
        return NotificationSettingsResponse.builder()
                .emailNotifications(settings.getEmailNotifications())
                .slackIntegration(settings.getSlackIntegration())
                .milestoneReminders(settings.getMilestoneReminders())
                .invoiceDueAlerts(settings.getInvoiceDueAlerts())
                .weeklyDigest(settings.getWeeklyDigest())
                .build();
    }
}
