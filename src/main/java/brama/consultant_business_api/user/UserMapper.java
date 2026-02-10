package brama.consultant_business_api.user;

import brama.consultant_business_api.auth.request.RegistrationRequest;
import brama.consultant_business_api.user.request.ProfileUpdateRequest;
import brama.consultant_business_api.user.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public User toUser(RegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .locked(false)
                .enabled(true)
                .credentialsExpired(false)
                .emailVerified(false)
                .phoneVerified(false)
                .build();
    }

    public void mergeUserInfo(final User savedUser, final ProfileUpdateRequest request) {
        if (StringUtils.isNotBlank(request.getFirstName())
                && !StringUtils.equals(savedUser.getFirstName(), request.getFirstName())) {
            savedUser.setFirstName(request.getFirstName());
        }

        if (StringUtils.isNotBlank(request.getLastName())
                && !StringUtils.equals(savedUser.getLastName(), request.getLastName())) {
            savedUser.setLastName(request.getLastName());
        }

        if (StringUtils.isNotBlank(request.getEmail())
                && !StringUtils.equalsIgnoreCase(savedUser.getEmail(), request.getEmail())) {
            savedUser.setEmail(request.getEmail());
        }

        if (StringUtils.isNotBlank(request.getPhone())
                && !StringUtils.equals(savedUser.getPhoneNumber(), request.getPhone())) {
            savedUser.setPhoneNumber(request.getPhone());
        }

        if (StringUtils.isNotBlank(request.getCompany())
                && !StringUtils.equals(savedUser.getCompany(), request.getCompany())) {
            savedUser.setCompany(request.getCompany());
        }

        if (StringUtils.isNotBlank(request.getLocation())
                && !StringUtils.equals(savedUser.getLocation(), request.getLocation())) {
            savedUser.setLocation(request.getLocation());
        }

        if (StringUtils.isNotBlank(request.getBio())
                && !StringUtils.equals(savedUser.getBio(), request.getBio())) {
            savedUser.setBio(request.getBio());
        }

        if (request.getDateOfBirth() != null
                && !request.getDateOfBirth().equals(savedUser.getDateOfBirth())) {
            savedUser.setDateOfBirth(request.getDateOfBirth());
        }
    }

    public UserProfileResponse toProfileResponse(final User user, final String roleName) {
        if (user == null) {
            return null;
        }
        return UserProfileResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .company(user.getCompany())
                .location(user.getLocation())
                .bio(user.getBio())
                .role(roleName)
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}