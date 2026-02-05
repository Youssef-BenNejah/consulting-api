package brama.consultant_business_api.domain.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ProjectType {
    FIXED("fixed"),
    TM("tm"),
    RETAINER("retainer");

    private final String value;

    ProjectType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProjectType fromValue(final String value) {
        return Arrays.stream(ProjectType.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown project type: " + value));
    }
}
