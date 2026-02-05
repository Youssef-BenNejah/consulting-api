package brama.consultant_business_api.domain.task.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum OwnerType {
    INTERNAL("internal"),
    VENDOR("vendor");

    private final String value;

    OwnerType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OwnerType fromValue(final String value) {
        return Arrays.stream(OwnerType.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown owner type: " + value));
    }
}
