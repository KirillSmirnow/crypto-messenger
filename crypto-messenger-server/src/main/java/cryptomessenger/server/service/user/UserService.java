package cryptomessenger.server.service.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface UserService {

    User register(@Valid UserRegistration registration);

    User getByUsername(@NotBlank String username);

    User getById(UUID id);
}
