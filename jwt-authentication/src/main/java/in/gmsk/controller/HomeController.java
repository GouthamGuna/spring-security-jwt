package in.gmsk.controller;

import in.gmsk.dto.AuthRequest;
import in.gmsk.model.UserInfo;
import in.gmsk.service.serviceImpl.JwtServiceImpl;
import in.gmsk.service.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final Logger logger =
            LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
    public String addNewUser(@RequestBody UserInfo userInfo) {
        logger.info("Inside the add new user method.");
        return userService.addUser(userInfo);
    }

    @PostMapping("/authenticate")
    public String authenticationAndGetToken(@RequestBody AuthRequest authRequest) {
        logger.info("Inside the authentication and get token method");
        Authentication authentication =
                authenticationManager.authenticate
                        (new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                                authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            logger.info("Token Generated Successfully!");
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request !");
        }
    }
}
