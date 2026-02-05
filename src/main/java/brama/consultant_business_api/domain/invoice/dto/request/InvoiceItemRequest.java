package brama.consultant_business_api.domain.invoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class InvoiceItemRequest {
    private String id;
    @NotBlank
    private String description;
    @NotNull
    @PositiveOrZero
    private Double quantity;
    @NotNull
    @PositiveOrZero
    private Double unitPrice;
    @PositiveOrZero
    private Double total;
}
