package brama.consultant_business_api.domain.settings.general.mapper;

import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.general.dto.response.GeneralSettingsResponse;
import brama.consultant_business_api.domain.settings.general.model.GeneralSettings;
import org.springframework.stereotype.Component;

@Component
public class GeneralSettingsMapper {
    public GeneralSettings toEntity(final GeneralSettingsUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return GeneralSettings.builder()
                .companyName(request.getCompanyName())
                .email(request.getEmail())
                .timezone(request.getTimezone())
                .defaultCurrency(request.getDefaultCurrency())
                .build();
    }

    public void merge(final GeneralSettings settings, final GeneralSettingsUpdateRequest request) {
        if (settings == null || request == null) {
            return;
        }
        settings.setCompanyName(request.getCompanyName());
        settings.setEmail(request.getEmail());
        settings.setTimezone(request.getTimezone());
        settings.setDefaultCurrency(request.getDefaultCurrency());
    }

    public GeneralSettingsResponse toResponse(final GeneralSettings settings) {
        if (settings == null) {
            return null;
        }
        return GeneralSettingsResponse.builder()
                .companyName(settings.getCompanyName())
                .email(settings.getEmail())
                .timezone(settings.getTimezone())
                .defaultCurrency(settings.getDefaultCurrency())
                .build();
    }
}
