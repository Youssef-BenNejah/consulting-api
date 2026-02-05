package brama.consultant_business_api.domain.milestone.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum MilestoneStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    OVERDUE("overdue");

    private final String value;

    MilestoneStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MilestoneStatus fromValue(final String value) {
        return Arrays.stream(MilestoneStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown milestone status: " + value));
    }
}
