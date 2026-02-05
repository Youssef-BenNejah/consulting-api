package brama.consultant_business_api.domain.invoice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum InvoiceStatus {
    DRAFT("draft"),
    SENT("sent"),
    PAID("paid"),
    OVERDUE("overdue"),
    CANCELLED("cancelled");

    private final String value;

    InvoiceStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static InvoiceStatus fromValue(final String value) {
        return Arrays.stream(InvoiceStatus.values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown invoice status: " + value));
    }
}
