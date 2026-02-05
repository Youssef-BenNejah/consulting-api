package brama.consultant_business_api.domain.contact.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ContactStatus {
    NEW("new"),
    READ("read"),
    IN_PROGRESS("in_progress"),
    REPLIED("replied"),
    CLOSED("closed");

    private final String value;

    ContactStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ContactStatus fromValue(final String value) {
        return Arrays.stream(ContactStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown contact status: " + value));
    }
}
