package brama.consultant_business_api.domain.communication.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum CommunicationType {
    CALL("call"),
    MEETING("meeting"),
    EMAIL("email");

    private final String value;

    CommunicationType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CommunicationType fromValue(final String value) {
        return Arrays.stream(CommunicationType.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown communication type: " + value));
    }
}
