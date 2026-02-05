package brama.consultant_business_api.domain.documentcategory.model;

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
@Document(collection = "document_categories")
public class DocumentCategory extends BaseDocument {
    @Field("name")
    private String name;

    @Field("key")
    private String key;

    @Field("color")
    private String color;
}
