package cryptomessenger.desktop.infrastructure.client.feign.user;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "user-client", url = "${app.server-base-url}")
public interface UserClient {

    @PostMapping("/users")
    UserDto register(@RequestBody UserRegistrationDto registration);

    @GetMapping("/users")
    @Cacheable("users")
    UserDto getByUsername(@RequestParam String username);

    @GetMapping("/users/{id}")
    @Cacheable("users")
    UserDto getById(@PathVariable UUID id);
}
