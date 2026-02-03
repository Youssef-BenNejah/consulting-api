package brama.consultant_business_api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class VerifyOtpRequest {
    @NotBlank(message = "VALIDATION.VERIFY_OTP.EMAIL.NOT_BLANK")
    @Email(message = "VALIDATION.VERIFY_OTP.EMAIL.FORMAT")
    @Schema(example = "user@example.com")
    private String email;

    @NotBlank(message = "VALIDATION.VERIFY_OTP.OTP.NOT_BLANK")
    @Pattern(regexp = "^\\d{6}$", message = "VALIDATION.VERIFY_OTP.OTP.FORMAT")
    @Schema(example = "123456")
    private String otp;
}
