package brama.consultant_business_api.domain.invoice.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invoices")
public class Invoice extends BaseDocument {
    @Field("invoice_number")
    private String invoiceNumber;

    @Field("type")
    private String type;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("client_id")
    private String clientId;

    @Field("client_name")
    private String clientName;

    @Field("client_address")
    private String clientAddress;

    @Field("date")
    private LocalDate date;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("amount")
    private Double amount;

    @Field("currency")
    private String currency;

    @Field("status")
    private InvoiceStatus status;

    @Field("notes")
    private String notes;

    @Field("items")
    private List<InvoiceItem> items;
}
