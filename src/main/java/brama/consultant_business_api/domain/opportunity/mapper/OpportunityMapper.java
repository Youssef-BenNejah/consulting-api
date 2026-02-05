package brama.consultant_business_api.domain.opportunity.mapper;

import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityCreateRequest;
import brama.consultant_business_api.domain.opportunity.dto.request.OpportunityUpdateRequest;
import brama.consultant_business_api.domain.opportunity.dto.response.OpportunityResponse;
import brama.consultant_business_api.domain.opportunity.model.Opportunity;
import org.springframework.stereotype.Component;

@Component
public class OpportunityMapper {
    public Opportunity toEntity(final OpportunityCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Opportunity.builder()
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .title(request.getTitle())
                .expectedValue(request.getExpectedValue())
                .probability(request.getProbability())
                .stage(request.getStage())
                .expectedCloseDate(request.getExpectedCloseDate())
                .notes(request.getNotes())
                .build();
    }

    public void merge(final Opportunity opportunity, final OpportunityUpdateRequest request) {
        if (opportunity == null || request == null) {
            return;
        }
        if (request.getClientId() != null) {
            opportunity.setClientId(request.getClientId());
        }
        if (request.getClientName() != null) {
            opportunity.setClientName(request.getClientName());
        }
        if (request.getTitle() != null) {
            opportunity.setTitle(request.getTitle());
        }
        if (request.getExpectedValue() != null) {
            opportunity.setExpectedValue(request.getExpectedValue());
        }
        if (request.getProbability() != null) {
            opportunity.setProbability(request.getProbability());
        }
        if (request.getStage() != null) {
            opportunity.setStage(request.getStage());
        }
        if (request.getExpectedCloseDate() != null) {
            opportunity.setExpectedCloseDate(request.getExpectedCloseDate());
        }
        if (request.getNotes() != null) {
            opportunity.setNotes(request.getNotes());
        }
    }

    public OpportunityResponse toResponse(final Opportunity opportunity) {
        if (opportunity == null) {
            return null;
        }
        return OpportunityResponse.builder()
                .id(opportunity.getId())
                .clientId(opportunity.getClientId())
                .clientName(opportunity.getClientName())
                .title(opportunity.getTitle())
                .expectedValue(opportunity.getExpectedValue())
                .probability(opportunity.getProbability())
                .stage(opportunity.getStage())
                .expectedCloseDate(opportunity.getExpectedCloseDate())
                .notes(opportunity.getNotes())
                .build();
    }
}
