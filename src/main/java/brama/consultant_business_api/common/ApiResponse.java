package brama.consultant_business_api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ApiResponse<T> {
    private T data;
    private PageMeta meta;
    @Builder.Default
    private List<ApiError> errors = Collections.emptyList();

    public static <T> ApiResponse<T> ok(final T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .errors(Collections.emptyList())
                .build();
    }

    public static <T> ApiResponse<T> of(final T data, final PageMeta meta) {
        return ApiResponse.<T>builder()
                .data(data)
                .meta(meta)
                .errors(Collections.emptyList())
                .build();
    }

    public static ApiResponse<Void> error(final List<ApiError> errors) {
        return ApiResponse.<Void>builder()
                .data(null)
                .errors(errors)
                .build();
    }
}
