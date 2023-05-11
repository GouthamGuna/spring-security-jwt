package in.gmsk.controller;

import in.gmsk.dto.AuthRequest;
import in.gmsk.dto.AuthResponseEntity;
import in.gmsk.dto.ErrorResponseEntity;
import in.gmsk.dto.SuccessResponseEntity;
import in.gmsk.model.UserInfo;
import in.gmsk.service.serviceImpl.JwtServiceImpl;
import in.gmsk.service.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HomeController {
    private final Logger logger =
            LoggerFactory.getLogger(HomeController.class);

    private final UserServiceImpl userService;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    public HomeController(UserServiceImpl userService, JwtServiceImpl jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String home() {
        logger.info("Inside the home method.");
        return "Welcome to our Alien website.!";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        logger.info("Inside the admin method.");
        return "All User List!";
    }

    @GetMapping("/parent")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String user() {
        logger.info("Inside the user method.");
        return "Student Details!";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity< ? > addNewUser(@RequestBody UserInfo userInfo) {
        logger.info("Inside the add new user method.");
        String output = userService.addUser(userInfo);
        return new ResponseEntity<>(new SuccessResponseEntity(output), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity< ? > authenticationAndGetToken(@RequestBody AuthRequest authRequest) {
        logger.info("Inside the authentication and get token method");
        if (authRequest.getUsername().isBlank() && authRequest.getUsername().isEmpty() &&
                authRequest.getPassword().isBlank() && authRequest.getPassword().isEmpty()) {
            Authentication authentication =
                    authenticationManager.authenticate
                            (new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                                    authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                logger.info("Token Generated Successfully!");
                String tokenValue = jwtService.generateToken(authRequest.getUsername());
                return new ResponseEntity<>(new AuthResponseEntity(tokenValue), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ErrorResponseEntity("Invalid user request!"), HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponseEntity("username and password cannot be empty!"), HttpStatus.BAD_REQUEST);
        }
    }
}
