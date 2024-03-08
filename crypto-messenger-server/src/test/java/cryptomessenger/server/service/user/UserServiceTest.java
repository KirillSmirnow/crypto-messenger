package cryptomessenger.server.service.user;

import cryptomessenger.server.service.UserException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void givenUserRegistration_whenRegister_thenUserSavedCorrectly() {
        var registration = UserRegistration.builder()
                .username("Kirill100")
                .publicKey(newPublicKey())
                .build();

        var returnedUser = userService.register(registration);

        var savedUser = userRepository.findById(returnedUser.getId());
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getId()).isNotNull();
        assertThat(savedUser.get().getUsername()).isEqualTo(registration.getUsername());
        assertThat(savedUser.get().getPublicKey()).isEqualTo(registration.getPublicKey());
    }

    @Test
    public void givenInvalidUserRegistration_whenRegister_thenConstraintViolationExceptionThrown() {
        var registration = UserRegistration.builder()
                .username("X")
                .build();

        var violations = assertThrows(
                ConstraintViolationException.class,
                () -> userService.register(registration)
        ).getConstraintViolations();
        assertThat(violations).hasSize(2);
    }

    @Test
    public void givenUserRegistrationWithExistingUsername_whenRegister_thenUserExceptionThrown() {
        var existingUser = saveUser();
        var registration = UserRegistration.builder()
                .username(existingUser.getUsername())
                .publicKey(newPublicKey())
                .build();

        assertThrows(
                UserException.class,
                () -> userService.register(registration)
        );
    }

    @Test
    public void givenUser_whenGetByUsername_thenCorrectUserReturned() {
        var existingUser = saveUser();

        var returnedUser = userService.getByUsername(existingUser.getUsername());

        assertThat(returnedUser).isEqualTo(existingUser).usingRecursiveComparison();
    }

    @Test
    public void givenNonExistentUsername_whenGetByUsername_thenUserExceptionThrown() {
        var nonExistentUsername = "123";

        assertThrows(
                UserException.class,
                () -> userService.getByUsername(nonExistentUsername)
        );
    }

    private User saveUser() {
        var user = new User(randomUUID());
        user.setUsername("Earth");
        user.setPublicKey(newPublicKey());
        return userRepository.save(user);
    }

    private byte[] newPublicKey() {
        var key = new byte[256];
        ThreadLocalRandom.current().nextBytes(key);
        return key;
    }

    @TestConfiguration
    @ComponentScan
    @Import(ValidationAutoConfiguration.class)
    public static class Configuration {
    }
}
