package brama.consultant_business_api.domain.document.dto.response;

import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.domain.document.enums.DocumentEntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private String id;
    private String name;
    private DocumentCategoryKey category;
    private String clientId;
    private String clientName;
    private String projectId;
    private String projectName;
    private DocumentEntityType entityType;
    private String entityId;
    private String uploadedBy;
    private LocalDate uploadedAt;
    private String size;
    private String fileType;
    private String fileId;
    private String fileUrl;
    private String storageProvider;
    private String resourceType;
}
