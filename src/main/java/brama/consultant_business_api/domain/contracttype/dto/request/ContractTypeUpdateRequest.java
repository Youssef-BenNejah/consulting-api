package brama.consultant_business_api.domain.contracttype.dto.request;

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
public class ContractTypeUpdateRequest {
    private String name;
    private String key;
    private String description;
}
