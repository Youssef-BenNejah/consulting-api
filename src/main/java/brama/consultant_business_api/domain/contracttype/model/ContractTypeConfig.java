package brama.consultant_business_api.domain.contracttype.model;

import brama.consultant_business_api.common.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contract_types")
public class ContractTypeConfig extends BaseDocument {
    @Field("name")
    private String name;

    @Field("key")
    private String key;

    @Field("description")
    private String description;
}
