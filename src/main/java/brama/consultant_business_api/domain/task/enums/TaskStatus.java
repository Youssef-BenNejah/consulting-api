package brama.consultant_business_api.domain.task.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    REVIEW("review"),
    DONE("done");

    private final String value;

    TaskStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TaskStatus fromValue(final String value) {
        return Arrays.stream(TaskStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown task status: " + value));
    }
}
