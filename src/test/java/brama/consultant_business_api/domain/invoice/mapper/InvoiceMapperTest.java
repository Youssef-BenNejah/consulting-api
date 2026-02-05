package brama.consultant_business_api.domain.invoice.mapper;

import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceItemRequest;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceMapperTest {
    private final InvoiceMapper mapper = new InvoiceMapper();

    @Test
    void toEntityCreatesItemsWithTotals() {
        InvoiceCreateRequest request = InvoiceCreateRequest.builder()
                .invoiceNumber("INV-1")
                .type("client")
                .projectId("p1")
                .projectName("Project")
                .clientId("c1")
                .clientName("Client")
                .date(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(5))
                .currency("EUR")
                .status(InvoiceStatus.SENT)
                .notes("Notes")
                .items(List.of(
                        InvoiceItemRequest.builder().description("A").quantity(2D).unitPrice(50D).build()
                ))
                .build();

        Invoice invoice = mapper.toEntity(request);
        assertThat(invoice.getItems()).hasSize(1);
        assertThat(invoice.getItems().get(0).getTotal()).isEqualTo(100D);
    }

    @Test
    void dueInfoMarksOverdue() {
        Invoice invoice = Invoice.builder()
                .id("i1")
                .invoiceNumber("INV-1")
                .status(InvoiceStatus.SENT)
                .dueDate(LocalDate.now().minusDays(1))
                .build();

        var response = mapper.toResponse(invoice);
        assertThat(response.getDueLabel()).isEqualTo("En retard");
        assertThat(response.getDueColor()).isEqualTo("text-health-red");
    }
}

