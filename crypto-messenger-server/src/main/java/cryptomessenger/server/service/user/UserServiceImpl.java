package cryptomessenger.server.service.user;

import cryptomessenger.server.service.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User register(UserRegistration registration) {
        if (userRepository.findByUsername(registration.getUsername()).isPresent()) {
            throw new UserException("Username already exists");
        }
        var user = new User(randomUUID());
        user.setUsername(registration.getUsername());
        user.setPublicKey(registration.getPublicKey());
        return userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("User not found"));
    }
}
