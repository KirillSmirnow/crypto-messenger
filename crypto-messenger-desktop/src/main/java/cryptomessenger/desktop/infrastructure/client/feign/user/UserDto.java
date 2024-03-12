package cryptomessenger.desktop.infrastructure.client.feign.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private final UUID id;
    private final String username;
    private final byte[] publicKey;
}
