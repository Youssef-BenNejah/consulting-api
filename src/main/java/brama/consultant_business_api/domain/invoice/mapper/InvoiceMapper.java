package brama.consultant_business_api.domain.invoice.mapper;

import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceItemRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceUpdateRequest;
import brama.consultant_business_api.domain.invoice.dto.response.InvoiceItemResponse;
import brama.consultant_business_api.domain.invoice.dto.response.InvoiceResponse;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.invoice.model.InvoiceItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class InvoiceMapper {
    public Invoice toEntity(final InvoiceCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Invoice.builder()
                .invoiceNumber(request.getInvoiceNumber())
                .type(request.getType())
                .projectId(request.getProjectId())
                .projectName(request.getProjectName())
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .clientAddress(request.getClientAddress())
                .date(request.getDate())
                .dueDate(request.getDueDate())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(request.getStatus())
                .notes(request.getNotes())
                .items(toItems(request.getItems()))
                .build();
    }

    public void merge(final Invoice invoice, final InvoiceUpdateRequest request) {
        if (invoice == null || request == null) {
            return;
        }
        if (request.getInvoiceNumber() != null) {
            invoice.setInvoiceNumber(request.getInvoiceNumber());
        }
        if (request.getType() != null) {
            invoice.setType(request.getType());
        }
        if (request.getProjectId() != null) {
            invoice.setProjectId(request.getProjectId());
        }
        if (request.getProjectName() != null) {
            invoice.setProjectName(request.getProjectName());
        }
        if (request.getClientId() != null) {
            invoice.setClientId(request.getClientId());
        }
        if (request.getClientName() != null) {
            invoice.setClientName(request.getClientName());
        }
        if (request.getClientAddress() != null) {
            invoice.setClientAddress(request.getClientAddress());
        }
        if (request.getDate() != null) {
            invoice.setDate(request.getDate());
        }
        if (request.getDueDate() != null) {
            invoice.setDueDate(request.getDueDate());
        }
        if (request.getAmount() != null) {
            invoice.setAmount(request.getAmount());
        }
        if (request.getCurrency() != null) {
            invoice.setCurrency(request.getCurrency());
        }
        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            invoice.setNotes(request.getNotes());
        }
        if (request.getItems() != null) {
            invoice.setItems(toItems(request.getItems()));
        }
    }

    public InvoiceResponse toResponse(final Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        final DueInfo dueInfo = computeDueInfo(invoice);
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .type(invoice.getType())
                .projectId(invoice.getProjectId())
                .projectName(invoice.getProjectName())
                .clientId(invoice.getClientId())
                .clientName(invoice.getClientName())
                .clientAddress(invoice.getClientAddress())
                .date(invoice.getDate())
                .dueDate(invoice.getDueDate())
                .amount(invoice.getAmount())
                .currency(invoice.getCurrency())
                .status(invoice.getStatus())
                .notes(invoice.getNotes())
                .items(toItemResponses(invoice.getItems()))
                .dueLabel(dueInfo.label)
                .dueColor(dueInfo.color)
                .build();
    }

    private List<InvoiceItem> toItems(final List<InvoiceItemRequest> requests) {
        if (requests == null) {
            return null;
        }
        final List<InvoiceItem> items = new ArrayList<>();
        for (final InvoiceItemRequest request : requests) {
            final String id = request.getId() != null ? request.getId() : UUID.randomUUID().toString();
            final Double quantity = request.getQuantity();
            final Double unitPrice = request.getUnitPrice();
            Double total = request.getTotal();
            if (total == null && quantity != null && unitPrice != null) {
                total = quantity * unitPrice;
            }
            items.add(InvoiceItem.builder()
                    .id(id)
                    .description(request.getDescription())
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .total(total)
                    .build());
        }
        return items;
    }

    private List<InvoiceItemResponse> toItemResponses(final List<InvoiceItem> items) {
        if (items == null) {
            return null;
        }
        final List<InvoiceItemResponse> responses = new ArrayList<>();
        for (final InvoiceItem item : items) {
            responses.add(InvoiceItemResponse.builder()
                    .id(item.getId())
                    .description(item.getDescription())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .total(item.getTotal())
                    .build());
        }
        return responses;
    }

    private DueInfo computeDueInfo(final Invoice invoice) {
        if (invoice.getDueDate() == null) {
            return new DueInfo(null, null);
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            return new DueInfo("Payee", "text-health-green");
        }
        final LocalDate today = LocalDate.now();
        final long days = ChronoUnit.DAYS.between(today, invoice.getDueDate());
        if (invoice.getStatus() == InvoiceStatus.OVERDUE || days < 0) {
            return new DueInfo("En retard", "text-health-red");
        }
        if (invoice.getStatus() == InvoiceStatus.SENT && days <= 7) {
            return new DueInfo(days + " jours", "text-health-amber");
        }
        return new DueInfo(invoice.getDueDate().toString(), "text-muted-foreground");
    }

    private static final class DueInfo {
        private final String label;
        private final String color;

        private DueInfo(final String label, final String color) {
            this.label = label;
            this.color = color;
        }
    }
}
