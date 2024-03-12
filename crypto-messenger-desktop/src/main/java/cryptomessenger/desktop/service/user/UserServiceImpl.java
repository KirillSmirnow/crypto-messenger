package cryptomessenger.desktop.service.user;

import cryptomessenger.coder.Coder;
import cryptomessenger.desktop.infrastructure.client.feign.user.UserClient;
import cryptomessenger.desktop.infrastructure.client.feign.user.UserRegistrationDto;
import cryptomessenger.desktop.infrastructure.localstorage.LocalStorage;
import cryptomessenger.desktop.service.LocalStorageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final LocalStorage localStorage;
    private final UserClient userClient;
    private final Coder coder;

    @Override
    public String getCurrentUsername() {
        return localStorage.getString(LocalStorageKeys.CURRENT_USERNAME);
    }

    @Override
    public void register(String username) {
        var keyPair = coder.generateKeyPair();
        userClient.register(
                UserRegistrationDto.builder()
                        .username(username)
                        .publicKey(keyPair.getPublicKey().getBytes())
                        .build()
        );
        localStorage.save(LocalStorageKeys.CURRENT_USERNAME, username);
        localStorage.save(LocalStorageKeys.PRIVATE_KEY, keyPair.getPrivateKey().getBytes());
    }
}
