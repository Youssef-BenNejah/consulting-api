package brama.consultant_business_api.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtpMailService {
    private final MailService mailService;

    public void sendOtp(final String email,
                        final String firstName,
                        final String otp,
                        final long expirationMinutes) {
        final Map<String, Object> variables = new HashMap<>();
        variables.put("firstName", (firstName != null && !firstName.isBlank()) ? firstName : "there");
        variables.put("otp", otp);
        variables.put("expirationMinutes", expirationMinutes);

        this.mailService.sendHtml(
                email,
                "Your OTP Code",
                "otp-email",
                variables
        );
    }
}
