package brama.consultant_business_api.auth;

import brama.consultant_business_api.auth.request.ForgotPasswordRequest;
import brama.consultant_business_api.auth.request.ResetPasswordRequest;
import brama.consultant_business_api.auth.request.VerifyOtpRequest;
import brama.consultant_business_api.auth.response.ForgotPasswordResponse;
import brama.consultant_business_api.auth.response.OtpResponse;
import brama.consultant_business_api.auth.response.ResetPasswordResponse;

public interface ForgotPasswordService {
    ForgotPasswordResponse requestOtp(ForgotPasswordRequest request);

    OtpResponse verifyOtp(VerifyOtpRequest request);

    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
}
