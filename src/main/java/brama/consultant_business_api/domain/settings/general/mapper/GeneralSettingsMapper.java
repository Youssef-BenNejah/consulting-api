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
                .darkMode(request.getDarkMode())
                .compactMode(request.getCompactMode())
                .build();
    }

    public void merge(final GeneralSettings settings, final GeneralSettingsUpdateRequest request) {
        if (settings == null || request == null) {
            return;
        }
        if (request.getCompanyName() != null) {
            settings.setCompanyName(request.getCompanyName());
        }
        if (request.getEmail() != null) {
            settings.setEmail(request.getEmail());
        }
        if (request.getTimezone() != null) {
            settings.setTimezone(request.getTimezone());
        }
        if (request.getDefaultCurrency() != null) {
            settings.setDefaultCurrency(request.getDefaultCurrency());
        }
        if (request.getDarkMode() != null) {
            settings.setDarkMode(request.getDarkMode());
        }
        if (request.getCompactMode() != null) {
            settings.setCompactMode(request.getCompactMode());
        }
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
                .darkMode(settings.getDarkMode())
                .compactMode(settings.getCompactMode())
                .build();
    }
}
