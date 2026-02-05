package brama.consultant_business_api.service.dashboard;

import brama.consultant_business_api.domain.dashboard.dto.response.DashboardNotification;
import brama.consultant_business_api.domain.dashboard.dto.response.DashboardSummaryResponse;

import java.util.List;

public interface DashboardService {
    DashboardSummaryResponse getSummary();

    List<DashboardNotification> getNotifications();
}
