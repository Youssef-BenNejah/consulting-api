package brama.consultant_business_api.domain.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ProjectStatus {
    DRAFT("draft"),
    DISCOVERY("discovery"),
    APPROVED("approved"),
    DELIVERY("delivery"),
    REVIEW("review"),
    DELIVERED("delivered"),
    CLOSED("closed"),
    ON_HOLD("on-hold"),
    CANCELLED("cancelled");

    private final String value;

    ProjectStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProjectStatus fromValue(final String value) {
        return Arrays.stream(ProjectStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown project status: " + value));
    }
}
