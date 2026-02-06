package brama.consultant_business_api.service.settings.impl;

import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.notification.dto.response.NotificationSettingsResponse;
import brama.consultant_business_api.domain.settings.notification.mapper.NotificationSettingsMapper;
import brama.consultant_business_api.domain.settings.notification.model.NotificationSettings;
import brama.consultant_business_api.repository.NotificationSettingsRepository;
import brama.consultant_business_api.service.settings.NotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSettingsServiceImpl implements NotificationSettingsService {
    private static final String SETTINGS_ID = "notifications";

    private final NotificationSettingsRepository repository;
    private final NotificationSettingsMapper mapper;

    @Override
    public NotificationSettingsResponse get() {
        return repository.findById(SETTINGS_ID)
                .map(mapper::toResponse)
                .orElseGet(this::defaultResponse);
    }

    @Override
    public NotificationSettingsResponse update(final NotificationSettingsUpdateRequest request) {
        final NotificationSettings settings = repository.findById(SETTINGS_ID)
                .orElseGet(() -> {
                    NotificationSettings created = NotificationSettings.builder()
                            .id(SETTINGS_ID)
                            .emailNotifications(true)
                            .slackIntegration(false)
                            .milestoneReminders(true)
                            .invoiceDueAlerts(true)
                            .weeklyDigest(true)
                            .build();
                    return created;
                });
        mapper.merge(settings, request);
        return mapper.toResponse(repository.save(settings));
    }

    private NotificationSettingsResponse defaultResponse() {
        return NotificationSettingsResponse.builder()
                .emailNotifications(true)
                .slackIntegration(false)
                .milestoneReminders(true)
                .invoiceDueAlerts(true)
                .weeklyDigest(true)
                .build();
    }
}
