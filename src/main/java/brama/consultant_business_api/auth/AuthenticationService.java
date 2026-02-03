package brama.consultant_business_api.auth;

import brama.consultant_business_api.auth.request.AuthenticationRequest;
import brama.consultant_business_api.auth.request.RefreshRequest;
import brama.consultant_business_api.auth.request.RegistrationRequest;
import brama.consultant_business_api.auth.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest req);
}

