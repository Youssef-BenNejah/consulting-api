package brama.consultant_business_api.domain.risk.mapper;

import brama.consultant_business_api.domain.risk.dto.request.RiskCreateRequest;
import brama.consultant_business_api.domain.risk.dto.request.RiskUpdateRequest;
import brama.consultant_business_api.domain.risk.enums.RiskStatus;
import brama.consultant_business_api.domain.risk.model.Risk;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class RiskMapperTest {
    private final RiskMapper mapper = new RiskMapper();

    @Test
    void createComputesScore() {
        RiskCreateRequest request = RiskCreateRequest.builder()
                .title("Risk")
                .description("Desc")
                .projectId("p1")
                .projectName("Project")
                .probability(50D)
                .impact(80D)
                .owner("Owner")
                .mitigationPlan("Plan")
                .dueDate(LocalDate.now())
                .status(RiskStatus.IDENTIFIED)
                .build();

        Risk risk = mapper.toEntity(request);
        assertThat(risk.getScore()).isEqualTo(40D);
    }

    @Test
    void mergeRecomputesScore() {
        Risk risk = Risk.builder().probability(50D).impact(80D).score(40D).build();

        mapper.merge(risk, RiskUpdateRequest.builder().probability(20D).build());

        assertThat(risk.getScore()).isEqualTo(16D);
    }
}

