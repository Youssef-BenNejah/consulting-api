package brama.consultant_business_api.domain.communication.mapper;

import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogCreateRequest;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogUpdateRequest;
import brama.consultant_business_api.domain.communication.dto.response.CommunicationLogResponse;
import brama.consultant_business_api.domain.communication.model.CommunicationLog;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CommunicationLogMapper {
    public CommunicationLog toEntity(final CommunicationLogCreateRequest request) {
        if (request == null) {
            return null;
        }
        return CommunicationLog.builder()
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .date(request.getDate())
                .type(request.getType())
                .summary(request.getSummary())
                .actionItems(request.getActionItems() != null ? new ArrayList<>(request.getActionItems()) : null)
                .participants(request.getParticipants() != null ? new ArrayList<>(request.getParticipants()) : null)
                .build();
    }

    public void merge(final CommunicationLog log, final CommunicationLogUpdateRequest request) {
        if (log == null || request == null) {
            return;
        }
        if (request.getClientId() != null) {
            log.setClientId(request.getClientId());
        }
        if (request.getClientName() != null) {
            log.setClientName(request.getClientName());
        }
        if (request.getProjectId() != null) {
            log.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            log.setProjectName(request.getProjectName());
        }
        if (request.getDate() != null) {
            log.setDate(request.getDate());
        }
        if (request.getType() != null) {
            log.setType(request.getType());
        }
        if (request.getSummary() != null) {
            log.setSummary(request.getSummary());
        }
        if (request.getActionItems() != null) {
            log.setActionItems(new ArrayList<>(request.getActionItems()));
        }
        if (request.getParticipants() != null) {
            log.setParticipants(new ArrayList<>(request.getParticipants()));
        }
    }

    public CommunicationLogResponse toResponse(final CommunicationLog log) {
        if (log == null) {
            return null;
        }
        return CommunicationLogResponse.builder()
                .id(log.getId())
                .clientId(log.getClientId())
                .clientName(log.getClientName())
                .projectId(log.getProjectId())
                .projectName(log.getProjectName())
                .date(log.getDate())
                .type(log.getType())
                .summary(log.getSummary())
                .actionItems(log.getActionItems())
                .participants(log.getParticipants())
                .build();
    }
}
