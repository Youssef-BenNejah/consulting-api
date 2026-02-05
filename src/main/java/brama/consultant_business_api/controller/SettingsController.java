package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.domain.settings.dto.request.SettingsPatchRequest;
import brama.consultant_business_api.domain.settings.dto.response.SettingsResponse;
import brama.consultant_business_api.service.settings.SettingsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "Settings API")
public class SettingsController {
    private final SettingsService service;

    @GetMapping
    public ApiResponse<SettingsResponse> getSettings() {
        return ApiResponse.ok(service.getAll());
    }

    @PatchMapping
    public ApiResponse<SettingsResponse> patchSettings(@Valid @RequestBody final SettingsPatchRequest request) {
        return ApiResponse.ok(service.patch(request));
    }
}
