package cryptomessenger.desktop.infrastructure.client.feign.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDto {
    private final String username;
    private final byte[] publicKey;
}
