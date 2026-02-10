package brama.consultant_business_api.user.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    @JsonAlias("confirmNewPassword")
    private String confirmPassword;
}
