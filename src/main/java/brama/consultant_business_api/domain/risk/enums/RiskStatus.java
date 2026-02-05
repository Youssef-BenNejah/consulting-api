package brama.consultant_business_api.domain.risk.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RiskStatus {
    IDENTIFIED("identified"),
    MITIGATING("mitigating"),
    MITIGATED("mitigated"),
    OCCURRED("occurred");

    private final String value;

    RiskStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RiskStatus fromValue(final String value) {
        return Arrays.stream(RiskStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown risk status: " + value));
    }
}
