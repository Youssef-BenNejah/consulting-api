package brama.consultant_business_api.domain.risk.mapper;

import brama.consultant_business_api.domain.risk.dto.request.RiskCreateRequest;
import brama.consultant_business_api.domain.risk.dto.request.RiskUpdateRequest;
import brama.consultant_business_api.domain.risk.dto.response.RiskResponse;
import brama.consultant_business_api.domain.risk.model.Risk;
import org.springframework.stereotype.Component;

@Component
public class RiskMapper {
    public Risk toEntity(final RiskCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Risk.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .probability(request.getProbability())
                .impact(request.getImpact())
                .score(computeScore(request.getProbability(), request.getImpact()))
                .owner(request.getOwner())
                .mitigationPlan(request.getMitigationPlan())
                .dueDate(request.getDueDate())
                .status(request.getStatus())
                .build();
    }

    public void merge(final Risk risk, final RiskUpdateRequest request) {
        if (risk == null || request == null) {
            return;
        }
        if (request.getTitle() != null) {
            risk.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            risk.setDescription(request.getDescription());
        }
        if (request.getProjectId() != null) {
            risk.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            risk.setProjectName(request.getProjectName());
        }
        if (request.getProbability() != null) {
            risk.setProbability(request.getProbability());
        }
        if (request.getImpact() != null) {
            risk.setImpact(request.getImpact());
        }
        if (request.getProbability() != null || request.getImpact() != null) {
            risk.setScore(computeScore(risk.getProbability(), risk.getImpact()));
        }
        if (request.getOwner() != null) {
            risk.setOwner(request.getOwner());
        }
        if (request.getMitigationPlan() != null) {
            risk.setMitigationPlan(request.getMitigationPlan());
        }
        if (request.getDueDate() != null) {
            risk.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null) {
            risk.setStatus(request.getStatus());
        }
    }

    public RiskResponse toResponse(final Risk risk) {
        if (risk == null) {
            return null;
        }
        return RiskResponse.builder()
                .id(risk.getId())
                .title(risk.getTitle())
                .description(risk.getDescription())
                .projectId(risk.getProjectId())
                .projectName(risk.getProjectName())
                .probability(risk.getProbability())
                .impact(risk.getImpact())
                .score(risk.getScore())
                .owner(risk.getOwner())
                .mitigationPlan(risk.getMitigationPlan())
                .dueDate(risk.getDueDate())
                .status(risk.getStatus())
                .build();
    }

    private double computeScore(final Double probability, final Double impact) {
        if (probability == null || impact == null) {
            return 0D;
        }
        return (probability * impact) / 100D;
    }
}
