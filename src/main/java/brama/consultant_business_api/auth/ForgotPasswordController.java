package brama.consultant_business_api.auth;

import brama.consultant_business_api.auth.request.ForgotPasswordRequest;
import brama.consultant_business_api.auth.request.ResetPasswordRequest;
import brama.consultant_business_api.auth.request.VerifyOtpRequest;
import brama.consultant_business_api.auth.response.ForgotPasswordResponse;
import brama.consultant_business_api.auth.response.OtpResponse;
import brama.consultant_business_api.auth.response.ResetPasswordResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/auth", "/api/v1/auth"})
@RequiredArgsConstructor
@Tag(name = "Password Reset", description = "Forgot password and OTP API")
public class ForgotPasswordController {
    private final ForgotPasswordService service;

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid
            @RequestBody
            final ForgotPasswordRequest request) {
        return ResponseEntity.ok(this.service.requestOtp(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpResponse> verifyOtp(
            @Valid
            @RequestBody
            final VerifyOtpRequest request) {
        return ResponseEntity.ok(this.service.verifyOtp(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @Valid
            @RequestBody
            final ResetPasswordRequest request) {
        return ResponseEntity.ok(this.service.resetPassword(request));
    }
}
