package brama.consultant_business_api.domain.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum StoryStatus {
    BACKLOG("backlog"),
    IN_PROGRESS("in-progress"),
    REVIEW("review"),
    APPROVED("approved"),
    REJECTED("rejected"),
    DONE("done");

    private final String value;

    StoryStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StoryStatus fromValue(final String value) {
        return Arrays.stream(StoryStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown story status: " + value));
    }
}
