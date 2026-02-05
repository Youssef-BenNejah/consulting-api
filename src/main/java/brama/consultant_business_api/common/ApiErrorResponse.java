package brama.consultant_business_api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API error response wrapper")
public class ApiErrorResponse {
    @Schema(nullable = true)
    private Object data;
    @Schema(nullable = true)
    private PageMeta meta;
    @Builder.Default
    private List<ApiError> errors = Collections.emptyList();
}
