package in.cerpsoft.service.impl;

import in.cerpsoft.configuration.JwtService;
import in.cerpsoft.entity.Role;
import in.cerpsoft.entity.UserEntity;
import in.cerpsoft.repository.UserRepository;
import in.cerpsoft.request.AuthenticationRequest;
import in.cerpsoft.request.RegisterRequest;
import in.cerpsoft.response.AuthenticationResponse;
import in.cerpsoft.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse registration(RegisterRequest request) {

        var user = UserEntity.builder()
                .userName(request.getUserName())
                .mobileNo(request.getMobileNo())
                .email(request.getEmail())
                .userSecret(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
