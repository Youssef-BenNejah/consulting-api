package brama.consultant_business_api.service.settings;

import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.general.dto.response.GeneralSettingsResponse;

public interface GeneralSettingsService {
    GeneralSettingsResponse get();

    GeneralSettingsResponse update(GeneralSettingsUpdateRequest request);
}
