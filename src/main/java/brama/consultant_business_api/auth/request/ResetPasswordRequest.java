package brama.consultant_business_api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class ResetPasswordRequest {
    @NotBlank(message = "VALIDATION.RESET_PASSWORD.EMAIL.NOT_BLANK")
    @Email(message = "VALIDATION.RESET_PASSWORD.EMAIL.FORMAT")
    @Schema(example = "user@example.com")
    private String email;

    @NotBlank(message = "VALIDATION.RESET_PASSWORD.OTP.NOT_BLANK")
    @Pattern(regexp = "^\\d{6}$", message = "VALIDATION.RESET_PASSWORD.OTP.FORMAT")
    @Schema(example = "123456")
    private String otp;

    @NotBlank(message = "VALIDATION.RESET_PASSWORD.PASSWORD.NOT_BLANK")
    @Size(min = 8, max = 72, message = "VALIDATION.RESET_PASSWORD.PASSWORD.SIZE")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).*$",
            message = "VALIDATION.RESET_PASSWORD.PASSWORD.WEAK")
    @Schema(example = "pAssword1!_")
    private String newPassword;

    @NotBlank(message = "VALIDATION.RESET_PASSWORD.CONFIRM_PASSWORD.NOT_BLANK")
    @Size(min = 8, max = 72, message = "VALIDATION.RESET_PASSWORD.CONFIRM_PASSWORD.SIZE")
    @Schema(example = "pAssword1!_")
    private String confirmPassword;
}
