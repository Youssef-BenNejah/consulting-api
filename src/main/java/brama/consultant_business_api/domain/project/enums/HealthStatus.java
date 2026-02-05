package brama.consultant_business_api.domain.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum HealthStatus {
    GREEN("green"),
    AMBER("amber"),
    RED("red");

    private final String value;

    HealthStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static HealthStatus fromValue(final String value) {
        return Arrays.stream(HealthStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown health status: " + value));
    }
}
