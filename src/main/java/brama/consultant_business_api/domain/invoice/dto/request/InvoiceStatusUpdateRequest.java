package brama.consultant_business_api.domain.invoice.dto.request;

import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import jakarta.validation.constraints.NotNull;
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
public class InvoiceStatusUpdateRequest {
    @NotNull
    private InvoiceStatus status;
}
