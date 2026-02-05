package brama.consultant_business_api.service.settings;

import brama.consultant_business_api.domain.settings.dto.request.SettingsPatchRequest;
import brama.consultant_business_api.domain.settings.dto.response.SettingsResponse;

public interface SettingsService {
    SettingsResponse getAll();

    SettingsResponse patch(SettingsPatchRequest request);
}
