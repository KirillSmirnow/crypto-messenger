package cryptomessenger.server.service.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistration {

    @NotBlank
    @Size(min = 3, max = 32)
    @Pattern(regexp = "[a-z0-9]*", flags = Flag.CASE_INSENSITIVE)
    private final String username;

    @NotEmpty
    @Size(min = 128, max = 1024)
    private final byte[] publicKey;
}
