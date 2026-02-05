package brama.consultant_business_api.service.invoice;

import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceItemRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceUpdateRequest;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.mapper.InvoiceMapper;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.invoice.model.InvoiceItem;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.service.invoice.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {
    @Mock
    private InvoiceRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    private InvoiceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new InvoiceServiceImpl(repository, mongoTemplate, new InvoiceMapper());
    }

    @Test
    void searchReturnsPagedResult() {
        Invoice invoice = Invoice.builder()
                .id("inv1")
                .invoiceNumber("INV-1")
                .type("client")
                .projectId("p1")
                .projectName("Project")
                .clientId("c1")
                .clientName("Client")
                .date(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(10))
                .amount(100D)
                .currency("EUR")
                .status(InvoiceStatus.SENT)
                .notes("Notes")
                .build();

        when(mongoTemplate.count(any(Query.class), eq(Invoice.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Invoice.class))).thenReturn(List.of(invoice));

        var result = service.search("inv", InvoiceStatus.SENT, "c1", "p1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 1, 10, "date,desc");
        assertThat(result.getTotal()).isEqualTo(1L);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createInvalidCurrencyThrows() {
        InvoiceCreateRequest request = baseRequest();
        request.setCurrency("AAA");

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void createInvalidDateRangeThrows() {
        InvoiceCreateRequest request = baseRequest();
        request.setDate(LocalDate.now());
        request.setDueDate(LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void createComputesAmountFromItems() {
        InvoiceCreateRequest request = baseRequest();
        request.setAmount(null);
        request.setItems(List.of(
                InvoiceItemRequest.builder().description("A").quantity(2D).unitPrice(50D).build(),
                InvoiceItemRequest.builder().description("B").quantity(1D).unitPrice(25D).build()
        ));

        when(repository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(request);
        assertThat(response.getAmount()).isEqualTo(125D);
        assertThat(response.getItems()).hasSize(2);
    }

    @Test
    void updateStatusUpdatesInvoice() {
        Invoice invoice = Invoice.builder()
                .id("inv1")
                .status(InvoiceStatus.DRAFT)
                .build();

        when(repository.findById("inv1")).thenReturn(Optional.of(invoice));
        when(repository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.updateStatus("inv1", InvoiceStatus.PAID);
        assertThat(response.getStatus()).isEqualTo(InvoiceStatus.PAID);
    }

    @Test
    void updateMissingThrows() {
        when(repository.findById("inv1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("inv1", new InvoiceUpdateRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void exportInvoicesCreatesZip() throws Exception {
        Invoice invoice = Invoice.builder()
                .id("inv1")
                .invoiceNumber("INV-1")
                .clientName("Client")
                .projectName("Project")
                .date(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(10))
                .status(InvoiceStatus.SENT)
                .amount(100D)
                .currency("EUR")
                .notes("Notes")
                .items(List.of(InvoiceItem.builder().id("it1").description("A").quantity(1D).unitPrice(100D).total(100D).build()))
                .build();

        when(repository.findAll()).thenReturn(List.of(invoice));

        byte[] zip = service.exportInvoices(null);

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip))) {
            assertThat(zis.getNextEntry()).isNotNull();
        }
    }

    private InvoiceCreateRequest baseRequest() {
        return InvoiceCreateRequest.builder()
                .invoiceNumber("INV-1")
                .type("client")
                .projectId("p1")
                .projectName("Project")
                .clientId("c1")
                .clientName("Client")
                .date(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(10))
                .amount(100D)
                .currency("EUR")
                .status(InvoiceStatus.SENT)
                .notes("Notes")
                .build();
    }
}

