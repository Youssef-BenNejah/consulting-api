package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardFinancialsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardNotificationsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardProjectHealthResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardStatsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardSummaryResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.UpcomingSchedulesResponse;
import brama.consultant_business_api.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/stats")
    public ApiResponse<DashboardStatsResponse> getStats() {
        return ApiResponse.ok(service.getStats());
    }

    @GetMapping("/project-health")
    public ApiResponse<DashboardProjectHealthResponse> getProjectHealth() {
        return ApiResponse.ok(service.getProjectHealth());
    }

    @GetMapping("/financials")
    public ApiResponse<DashboardFinancialsResponse> getFinancials() {
        return ApiResponse.ok(service.getFinancials());
    }

    @GetMapping("/notifications")
    public ApiResponse<DashboardNotificationsResponse> getNotifications() {
        return ApiResponse.ok(service.getNotifications());
    }

    @GetMapping("/upcoming-schedules")
    public ApiResponse<UpcomingSchedulesResponse> getUpcomingSchedules(
            @RequestParam(defaultValue = "14") final int days,
            @RequestParam(defaultValue = "5") final int limit) {
        return ApiResponse.ok(service.getUpcomingSchedules(days, limit));
    }
}
