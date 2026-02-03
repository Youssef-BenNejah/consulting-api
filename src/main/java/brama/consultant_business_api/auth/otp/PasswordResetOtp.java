package brama.consultant_business_api.auth.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "password_reset_otps")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetOtp {
    @Id
    private String id;

    @Field("email")
    @Indexed
    private String email;

    @Field("otp_hash")
    private String otpHash;

    @Field("created_at")
    private Instant createdAt;

    @Field("expires_at")
    private Instant expiresAt;

    @Field("used")
    private boolean used;
}
