package brama.consultant_business_api.domain.contracttype.dto.response;

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
public class ContractTypeResponse {
    private String id;
    private String name;
    private String key;
    private String description;

}
