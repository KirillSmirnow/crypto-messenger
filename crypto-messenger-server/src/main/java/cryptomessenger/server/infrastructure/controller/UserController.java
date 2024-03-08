package cryptomessenger.server.infrastructure.controller;

import cryptomessenger.server.service.user.User;
import cryptomessenger.server.service.user.UserRegistration;
import cryptomessenger.server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public User register(@RequestBody UserRegistration registration) {
        return userService.register(registration);
    }

    @GetMapping("/users/{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }
}
