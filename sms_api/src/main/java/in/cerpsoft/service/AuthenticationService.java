package in.cerpsoft.service;

import in.cerpsoft.request.AuthenticationRequest;
import in.cerpsoft.request.RegisterRequest;
import in.cerpsoft.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse registration(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
