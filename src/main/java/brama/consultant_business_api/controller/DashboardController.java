package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardNotification;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardSummaryResponse;
import brama.consultant_business_api.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard API")
public class DashboardController {
    private final DashboardService service;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        return ApiResponse.ok(service.getSummary());
    }

    @GetMapping("/notifications")
    public ApiResponse<List<DashboardNotification>> getNotifications() {
        return ApiResponse.ok(service.getNotifications());
    }
}
