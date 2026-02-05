package brama.consultant_business_api.service.settings;

import brama.consultant_business_api.domain.settings.notification.dto.request.NotificationSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.notification.dto.response.NotificationSettingsResponse;

public interface NotificationSettingsService {
    NotificationSettingsResponse get();

    NotificationSettingsResponse update(NotificationSettingsUpdateRequest request);
}
