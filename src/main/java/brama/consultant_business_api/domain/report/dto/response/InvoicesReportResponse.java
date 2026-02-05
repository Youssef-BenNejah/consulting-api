package brama.consultant_business_api.domain.report.dto.response;

import brama.consultant_business_api.domain.invoice.dto.response.InvoiceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicesReportResponse {
    private double totalReceivables;
    private long overdueInvoices;
    private long totalInvoices;
    private List<InvoiceResponse> overdue;
}
