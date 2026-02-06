package brama.consultant_business_api.service.settings.impl;

import brama.consultant_business_api.domain.settings.general.dto.request.GeneralSettingsUpdateRequest;
import brama.consultant_business_api.domain.settings.general.dto.response.GeneralSettingsResponse;
import brama.consultant_business_api.domain.settings.general.mapper.GeneralSettingsMapper;
import brama.consultant_business_api.domain.settings.general.model.GeneralSettings;
import brama.consultant_business_api.repository.GeneralSettingsRepository;
import brama.consultant_business_api.service.settings.GeneralSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralSettingsServiceImpl implements GeneralSettingsService {
    private static final String SETTINGS_ID = "general";

    private final GeneralSettingsRepository repository;
    private final GeneralSettingsMapper mapper;

    @Override
    public GeneralSettingsResponse get() {
        return repository.findById(SETTINGS_ID)
                .map(mapper::toResponse)
                .orElseGet(this::defaultResponse);
    }

    @Override
    public GeneralSettingsResponse update(final GeneralSettingsUpdateRequest request) {
        final GeneralSettings settings = repository.findById(SETTINGS_ID)
                .orElseGet(() -> {
                    GeneralSettings created = GeneralSettings.builder()
                            .id(SETTINGS_ID)
                            .companyName("Cherif Consulting")
                            .email(null)
                            .timezone("Europe/Paris")
                            .defaultCurrency("EUR")
                            .darkMode(true)
                            .compactMode(false)
                            .build();
                    return created;
                });
        mapper.merge(settings, request);
        return mapper.toResponse(repository.save(settings));
    }

    private GeneralSettingsResponse defaultResponse() {
        return GeneralSettingsResponse.builder()
                .companyName("Cherif Consulting")
                .email(null)
                .timezone("Europe/Paris")
                .defaultCurrency("EUR")
                .darkMode(true)
                .compactMode(false)
                .build();
    }
}
