package brama.consultant_business_api.domain.settings.dto.request.items;

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
public class DocumentCategoryUpsertRequest {
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String key;
    @NotBlank
    private String color;
}
