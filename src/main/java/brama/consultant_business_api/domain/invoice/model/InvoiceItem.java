package brama.consultant_business_api.domain.invoice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    private String id;
    private String description;
    private Double quantity;
    private Double unitPrice;
    private Double total;
}
