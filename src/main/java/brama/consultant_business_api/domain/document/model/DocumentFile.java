package brama.consultant_business_api.domain.document.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.domain.document.enums.DocumentEntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "documents")
public class DocumentFile extends BaseDocument {
    @Field("name")
    private String name;

    @Field("category")
    private DocumentCategoryKey category;

    @Field("client_id")
    private String clientId;

    @Field("client_name")
    private String clientName;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("entity_type")
    private DocumentEntityType entityType;

    @Field("entity_id")
    private String entityId;

    @Field("uploaded_by")
    private String uploadedBy;

    @Field("uploaded_at")
    private LocalDate uploadedAt;

    @Field("size")
    private String size;

    @Field("file_type")
    private String fileType;

    @Field("file_id")
    private String fileId;

    @Field("file_url")
    private String fileUrl;

    @Field("storage_provider")
    private String storageProvider;

    @Field("resource_type")
    private String resourceType;
}
