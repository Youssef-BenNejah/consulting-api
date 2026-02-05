package brama.consultant_business_api.domain.issue.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum IssueStatus {
    OPEN("open"),
    IN_PROGRESS("in-progress"),
    RESOLVED("resolved"),
    CLOSED("closed");

    private final String value;

    IssueStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static IssueStatus fromValue(final String value) {
        return Arrays.stream(IssueStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown issue status: " + value));
    }
}
