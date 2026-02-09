package brama.consultant_business_api.service.dashboard;

import brama.consultant_business_api.domain.dashboard.dto.response.DashboardNotificationsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardFinancialsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardProjectHealthResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardStatsResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardSummaryResponse;
import brama.consultant_business_api.domain.dashboard.dto.response.UpcomingSchedulesResponse;

public interface DashboardService {
    DashboardSummaryResponse getSummary();

    DashboardStatsResponse getStats();

    DashboardNotificationsResponse getNotifications();

    DashboardProjectHealthResponse getProjectHealth();

    DashboardFinancialsResponse getFinancials();

    UpcomingSchedulesResponse getUpcomingSchedules(int days, int limit);
}
