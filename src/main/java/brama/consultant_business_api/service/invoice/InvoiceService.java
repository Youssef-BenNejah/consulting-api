package brama.consultant_business_api.service.invoice;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceUpdateRequest;
import brama.consultant_business_api.domain.invoice.dto.response.InvoiceResponse;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.dashboard.dto.response.FinancialsResponse;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    PagedResult<InvoiceResponse> search(String search,
                                        InvoiceStatus status,
                                        String clientId,
                                        String projectId,
                                        LocalDate dateFrom,
                                        LocalDate dateTo,
                                        Integer page,
                                        Integer size,
                                        String sort);

    InvoiceResponse create(InvoiceCreateRequest request);

    InvoiceResponse getById(String id);

    InvoiceResponse update(String id, InvoiceUpdateRequest request);

    InvoiceResponse updateStatus(String id, InvoiceStatus status);

    void delete(String id);

    byte[] exportInvoices(List<String> ids);

    FinancialsResponse getProjectFinancials(String projectId);
}
