package in.dev.ggs.controller;

import in.dev.ggs.dto.AuthRequest;
import in.dev.ggs.dto.AuthResponseEntity;
import in.dev.ggs.dto.ErrorResponseEntity;
import in.dev.ggs.util.JwtServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(JwtServiceImpl jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticationAndGetToken(@RequestBody AuthRequest authRequest) {
        if (!authRequest.getUsername().isBlank() && !authRequest.getUsername().isEmpty() &&
                !authRequest.getPassword().isBlank() && !authRequest.getPassword().isEmpty()) {
            Authentication authentication =
                    authenticationManager.authenticate
                            (new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                                    authRequest.getPassword()));
            return getResponseEntity(authRequest, authentication);
        } else {
            return new ResponseEntity<>(new ErrorResponseEntity("username and password cannot be empty!"), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> getResponseEntity(AuthRequest authRequest, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            String tokenValue = jwtService.generateToken(authRequest.getUsername());
            return new ResponseEntity<>(new AuthResponseEntity(tokenValue), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ErrorResponseEntity("Invalid user request!"), HttpStatus.CONFLICT);
        }
    }
}
