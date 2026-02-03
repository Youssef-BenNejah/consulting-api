package brama.consultant_business_api.auth.otp;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends MongoRepository<PasswordResetOtp, String> {
    Optional<PasswordResetOtp> findTopByEmailIgnoreCaseOrderByCreatedAtDesc(String email);
}
