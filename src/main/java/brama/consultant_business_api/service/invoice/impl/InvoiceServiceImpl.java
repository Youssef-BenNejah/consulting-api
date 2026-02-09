package brama.consultant_business_api.service.invoice.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.common.SortUtils;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceUpdateRequest;
import brama.consultant_business_api.domain.invoice.dto.response.InvoiceResponse;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.invoice.mapper.InvoiceMapper;
import brama.consultant_business_api.domain.invoice.model.Invoice;
import brama.consultant_business_api.domain.invoice.model.InvoiceItem;
import brama.consultant_business_api.domain.dashboard.dto.response.FinancialsResponse;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.exception.ErrorCode;
import brama.consultant_business_api.repository.InvoiceRepository;
import brama.consultant_business_api.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("EUR", "CHF", "USD", "GBP");

    private final InvoiceRepository repository;
    private final MongoTemplate mongoTemplate;
    private final InvoiceMapper mapper;

    @Override
    public PagedResult<InvoiceResponse> search(final String search,
                                               final InvoiceStatus status,
                                               final String clientId,
                                               final String projectId,
                                               final LocalDate dateFrom,
                                               final LocalDate dateTo,
                                               final Integer page,
                                               final Integer size,
                                               final String sort) {
        final Query query = new Query();
        QueryUtils.addRegexOrCriteria(query, search, "invoiceNumber", "projectName", "clientName");
        QueryUtils.addIfNotNull(query, "status", status);
        QueryUtils.addIfNotBlank(query, "clientId", clientId);
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        QueryUtils.addDateRange(query, "date", dateFrom, dateTo);
        final long total = mongoTemplate.count(query, Invoice.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, SortUtils.parseSort(sort));
        query.with(pageable);
        final List<InvoiceResponse> items = mongoTemplate.find(query, Invoice.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<InvoiceResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public InvoiceResponse create(final InvoiceCreateRequest request) {
        final Invoice invoice = mapper.toEntity(request);
        normalizeInvoice(invoice);
        final Invoice saved = repository.save(invoice);
        return mapper.toResponse(saved);
    }

    @Override
    public InvoiceResponse getById(final String id) {
        final Invoice invoice = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + id));
        return mapper.toResponse(invoice);
    }

    @Override
    public InvoiceResponse update(final String id, final InvoiceUpdateRequest request) {
        final Invoice invoice = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + id));
        mapper.merge(invoice, request);
        normalizeInvoice(invoice);
        final Invoice saved = repository.save(invoice);
        return mapper.toResponse(saved);
    }

    @Override
    public InvoiceResponse updateStatus(final String id, final InvoiceStatus status) {
        final Invoice invoice = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + id));
        invoice.setStatus(status);
        final Invoice saved = repository.save(invoice);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Invoice not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public byte[] exportInvoices(final List<String> ids) {
        final List<Invoice> invoices = (ids == null || ids.isEmpty())
                ? repository.findAll()
                : repository.findAllById(ids);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (final Invoice invoice : invoices) {
                final String html = buildInvoiceHtml(invoice);
                final String filename = (invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : invoice.getId()) + ".html";
                final ZipEntry entry = new ZipEntry(filename);
                zos.putNextEntry(entry);
                zos.write(html.getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.INTERNAL_EXCEPTION, ex.getMessage());
        }
    }

    @Override
    public FinancialsResponse getProjectFinancials(final String projectId) {
        final Query query = new Query();
        QueryUtils.addIfNotBlank(query, "projectId", projectId);
        final List<Invoice> invoices = mongoTemplate.find(query, Invoice.class);
        final double totalBilled = invoices.stream()
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();
        final double totalReceived = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();
        final double outstandingReceivables = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.SENT || i.getStatus() == InvoiceStatus.OVERDUE)
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0D)
                .sum();
        return FinancialsResponse.builder()
                .totalBilled(totalBilled)
                .totalReceived(totalReceived)
                .outstandingReceivables(outstandingReceivables)
                .build();
    }

    private void normalizeInvoice(final Invoice invoice) {
        validateInvoice(invoice);
        if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            double total = 0D;
            for (final InvoiceItem item : invoice.getItems()) {
                if (item.getId() == null) {
                    item.setId(UUID.randomUUID().toString());
                }
                Double itemTotal = item.getTotal();
                if (itemTotal == null && item.getQuantity() != null && item.getUnitPrice() != null) {
                    itemTotal = item.getQuantity() * item.getUnitPrice();
                    item.setTotal(itemTotal);
                }
                if (itemTotal != null) {
                    total += itemTotal;
                }
            }
            invoice.setAmount(total);
        } else if (invoice.getAmount() == null) {
            invoice.setAmount(0D);
        }
    }

    private void validateInvoice(final Invoice invoice) {
        if (invoice.getCurrency() != null && !SUPPORTED_CURRENCIES.contains(invoice.getCurrency())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENCY, invoice.getCurrency());
        }
        if (invoice.getDate() != null && invoice.getDueDate() != null && invoice.getDueDate().isBefore(invoice.getDate())) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE, "dueDate before date");
        }
    }

    private String buildInvoiceHtml(final Invoice invoice) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset=\"UTF-8\"><title>Invoice ")
                .append(invoice.getInvoiceNumber())
                .append("</title></head><body>");
        sb.append("<h1>Invoice ").append(invoice.getInvoiceNumber()).append("</h1>");
        sb.append("<p>Client: ").append(invoice.getClientName()).append("</p>");
        sb.append("<p>Project: ").append(invoice.getProjectName()).append("</p>");
        sb.append("<p>Date: ").append(invoice.getDate()).append("</p>");
        sb.append("<p>Due: ").append(invoice.getDueDate()).append("</p>");
        sb.append("<p>Status: ").append(invoice.getStatus()).append("</p>");
        if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            sb.append("<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">");
            sb.append("<tr><th>Description</th><th>Qty</th><th>Unit Price</th><th>Total</th></tr>");
            for (final InvoiceItem item : invoice.getItems()) {
                sb.append("<tr>")
                        .append("<td>").append(item.getDescription()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>").append(item.getUnitPrice()).append("</td>")
                        .append("<td>").append(item.getTotal()).append("</td>")
                        .append("</tr>");
            }
            sb.append("</table>");
        }
        sb.append("<p>Total: ").append(invoice.getAmount()).append(" ").append(invoice.getCurrency()).append("</p>");
        sb.append("<p>Notes: ").append(invoice.getNotes()).append("</p>");
        sb.append("</body></html>");
        return sb.toString();
    }
}
