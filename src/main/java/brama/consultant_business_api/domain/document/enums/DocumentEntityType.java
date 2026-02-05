package brama.consultant_business_api.domain.document.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentEntityType {
    CLIENT("clients"),
    PROJECT("projects"),
    INVOICE("invoices"),
    DOCUMENT("documents"),
    OTHER("other");

    private final String folderName;
}
