package brama.consultant_business_api.domain.documentcategory.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class DocumentCategoryCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String key;
    @NotBlank
    private String color;
}
