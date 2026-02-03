package brama.consultant_business_api.auth.impl;

import brama.consultant_business_api.auth.ForgotPasswordService;
import brama.consultant_business_api.auth.otp.PasswordResetOtp;
import brama.consultant_business_api.auth.otp.PasswordResetOtpRepository;
import brama.consultant_business_api.auth.request.ForgotPasswordRequest;
import brama.consultant_business_api.auth.request.ResetPasswordRequest;
import brama.consultant_business_api.auth.request.VerifyOtpRequest;
import brama.consultant_business_api.auth.response.ForgotPasswordResponse;
import brama.consultant_business_api.auth.response.OtpResponse;
import brama.consultant_business_api.auth.response.ResetPasswordResponse;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.ErrorCode;
import brama.consultant_business_api.mail.OtpMailService;
import brama.consultant_business_api.user.User;
import brama.consultant_business_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int OTP_MIN = 100000;
    private static final int OTP_RANGE = 900000;

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpMailService otpMailService;

    @Value("${app.security.otp.expiration-minutes:10}")
    private long expirationMinutes;

    @Override
    public ForgotPasswordResponse requestOtp(final ForgotPasswordRequest request) {
        final User user = this.userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        final String otp = generateOtp();
        final PasswordResetOtp otpEntity = PasswordResetOtp.builder()
                .email(user.getEmail())
                .otpHash(this.passwordEncoder.encode(otp))
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(this.expirationMinutes, ChronoUnit.MINUTES))
                .used(false)
                .build();
        this.otpRepository.save(otpEntity);

        this.otpMailService.sendOtp(user.getEmail(), user.getFirstName(), otp, this.expirationMinutes);

        return ForgotPasswordResponse.builder()
                .message("OTP sent to your email")
                .email(user.getEmail())
                .build();
    }

    @Override
    public OtpResponse verifyOtp(final VerifyOtpRequest request) {
        final PasswordResetOtp otpEntity = getLatestOtp(request.getEmail());
        validateOtp(otpEntity, request.getOtp());
        return OtpResponse.builder()
                .verified(true)
                .message("OTP verified successfully")
                .build();
    }

    @Override
    public ResetPasswordResponse resetPassword(final ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }

        final User user = this.userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        final PasswordResetOtp otpEntity = getLatestOtp(request.getEmail());
        validateOtp(otpEntity, request.getOtp());

        user.setPassword(this.passwordEncoder.encode(request.getNewPassword()));
        this.userRepository.save(user);

        otpEntity.setUsed(true);
        this.otpRepository.save(otpEntity);

        return ResetPasswordResponse.builder()
                .success(true)
                .message("Password reset successfully")
                .build();
    }

    private PasswordResetOtp getLatestOtp(final String email) {
        return this.otpRepository.findTopByEmailIgnoreCaseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.OTP_NOT_FOUND));
    }

    private void validateOtp(final PasswordResetOtp otpEntity, final String rawOtp) {
        if (otpEntity.isUsed()) {
            throw new BusinessException(ErrorCode.OTP_ALREADY_USED);
        }
        if (otpEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.OTP_EXPIRED);
        }
        if (!this.passwordEncoder.matches(rawOtp, otpEntity.getOtpHash())) {
            throw new BusinessException(ErrorCode.OTP_INVALID);
        }
    }

    private String generateOtp() {
        final int code = OTP_MIN + SECURE_RANDOM.nextInt(OTP_RANGE);
        return Integer.toString(code);
    }
}
