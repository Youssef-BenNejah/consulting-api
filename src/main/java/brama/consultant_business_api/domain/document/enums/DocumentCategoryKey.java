package brama.consultant_business_api.domain.document.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum DocumentCategoryKey {
    CONTRACT("contract"),
    SOW("sow"),
    PROPOSAL("proposal"),
    REQUIREMENTS("requirements"),
    DESIGN("design"),
    TEST_REPORT("test-report"),
    INVOICE("invoice"),
    MEETING_NOTES("meeting-notes");

    private final String value;

    DocumentCategoryKey(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static DocumentCategoryKey fromValue(final String value) {
        return Arrays.stream(DocumentCategoryKey.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown document category key: " + value));
    }
}
