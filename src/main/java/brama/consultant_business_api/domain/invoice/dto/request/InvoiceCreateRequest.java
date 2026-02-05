package brama.consultant_business_api.domain.invoice.dto.request;

import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class InvoiceCreateRequest {
    @NotBlank
    private String invoiceNumber;
    @NotBlank
    private String type;
    @NotBlank
    private String projectId;
    @NotBlank
    private String projectName;
    private String clientId;
    @NotBlank
    private String clientName;
    private String clientAddress;
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalDate dueDate;
    @PositiveOrZero
    private Double amount;
    @NotBlank
    private String currency;
    @NotNull
    private InvoiceStatus status;
    @NotBlank
    private String notes;
    @Valid
    private List<InvoiceItemRequest> items;
}
