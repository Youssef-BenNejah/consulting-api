package brama.consultant_business_api.domain.invoice.dto.response;

import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private String id;
    private String invoiceNumber;
    private String type;
    private String projectId;
    private String projectName;
    private String clientId;
    private String clientName;
    private String clientAddress;
    private LocalDate date;
    private LocalDate dueDate;
    private Double amount;
    private String currency;
    private InvoiceStatus status;
    private String notes;
    private List<InvoiceItemResponse> items;
    private String dueLabel;
    private String dueColor;
}
