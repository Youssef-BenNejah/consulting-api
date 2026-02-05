package brama.consultant_business_api.domain.document.dto.request;

import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.domain.document.enums.DocumentEntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class DocumentCreateRequest {
    private String name;
    @NotNull
    private DocumentCategoryKey category;
    private String clientId;
    private String clientName;
    private String projectId;
    private String projectName;
    private DocumentEntityType entityType;
    private String entityId;
    @NotBlank
    private String uploadedBy;
    private LocalDate uploadedAt;
    private String size;
    private String fileType;
}
