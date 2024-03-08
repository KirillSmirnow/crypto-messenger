package cryptomessenger.server.infrastructure.controller;

import cryptomessenger.server.service.user.User;
import cryptomessenger.server.service.user.UserRegistration;
import cryptomessenger.server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public User register(@RequestBody UserRegistration registration) {
        return userService.register(registration);
    }

    @GetMapping("/users")
    public User getByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable UUID id) {
        return userService.getById(id);
    }
}
