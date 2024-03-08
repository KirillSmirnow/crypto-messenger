package cryptomessenger.server.service.user;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
public class User {

    private final UUID id;

    @Indexed(unique = true)
    private String username;

    private byte[] publicKey;
}
