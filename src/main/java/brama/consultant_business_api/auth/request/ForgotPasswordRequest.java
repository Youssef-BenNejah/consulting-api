package brama.consultant_business_api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordRequest {
    @NotBlank(message = "VALIDATION.FORGOT_PASSWORD.EMAIL.NOT_BLANK")
    @Email(message = "VALIDATION.FORGOT_PASSWORD.EMAIL.FORMAT")
    @Schema(example = "user@example.com")
    private String email;
}
