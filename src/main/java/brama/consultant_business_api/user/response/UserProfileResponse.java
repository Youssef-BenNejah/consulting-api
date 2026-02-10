package brama.consultant_business_api.user.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String location;
    private String bio;
    private String role;
    private String avatarUrl;
}