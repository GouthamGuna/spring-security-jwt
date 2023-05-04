package in.gmsk.controller;

import in.gmsk.model.UserInfo;
import in.gmsk.service.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final Logger logger =
            LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/welcome")
    public String home() {
        return "Welcome to CERPSOFT !";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "All User List!";
    }

    @GetMapping("/parent")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String user() {
        return "Student Details!";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return userService.addUser(userInfo);
    }
}
