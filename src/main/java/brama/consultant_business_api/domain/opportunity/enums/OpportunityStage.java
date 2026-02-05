package brama.consultant_business_api.domain.opportunity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum OpportunityStage {
    LEAD("lead"),
    QUALIFIED("qualified"),
    PROPOSAL("proposal"),
    NEGOTIATION("negotiation"),
    WON("won"),
    LOST("lost");

    private final String value;

    OpportunityStage(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OpportunityStage fromValue(final String value) {
        return Arrays.stream(OpportunityStage.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown opportunity stage: " + value));
    }
}
