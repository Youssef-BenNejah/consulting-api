package brama.consultant_business_api.auth.impl;

import brama.consultant_business_api.auth.AuthenticationService;
import brama.consultant_business_api.auth.request.AuthenticationRequest;
import brama.consultant_business_api.auth.request.RefreshRequest;
import brama.consultant_business_api.auth.request.RegistrationRequest;
import brama.consultant_business_api.auth.response.AuthenticationResponse;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.exception.ErrorCode;
import brama.consultant_business_api.role.Role;
import brama.consultant_business_api.role.RoleRepository;
import brama.consultant_business_api.security.JwtService;
import brama.consultant_business_api.user.User;
import brama.consultant_business_api.user.UserMapper;
import brama.consultant_business_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        final User user = (User) auth.getPrincipal();
        final String accessToken = this.jwtService.generateAccessToken(user);
        final String refreshToken=this.jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        checkUserEmail(request.getEmail());
        checkUserPhoneNumber(request.getPhoneNumber());
        checkPassword(request.getPassword(), request.getConfirmPassword());

        final Role userRole = this.roleRepository.findByName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Role USER does not exist"));

        final List<String> roleIds = new ArrayList<>();
        roleIds.add(userRole.getId());

        final User user = this.userMapper.toUser(request);
        user.setRoles(roleIds);

        log.debug("Saving user {}", user);
        this.userRepository.save(user);

    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest req) {
        final String accessToken = this.jwtService.refreshAccessToken(req.getRefreshToken());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(req.getRefreshToken())
                .tokenType("Bearer")
                .build();

    }
    private void checkPassword(String password, String confirmPassword) {
        if(password == null || !password.equals(confirmPassword))
        {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

    private void checkUserPhoneNumber(String phoneNumber) {
        final boolean phoneNumberExists = this.userRepository.existsByPhoneNumber(phoneNumber);
        if (phoneNumberExists) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    private void checkUserEmail(String email) {
        final boolean emailExists = this.userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

    }
}

