package brama.consultant_business_api.domain.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Priority {
    MUST("must"),
    SHOULD("should"),
    COULD("could");

    private final String value;

    Priority(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Priority fromValue(final String value) {
        return Arrays.stream(Priority.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown priority: " + value));
    }
}
