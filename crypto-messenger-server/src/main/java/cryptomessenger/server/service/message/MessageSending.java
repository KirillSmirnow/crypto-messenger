package cryptomessenger.server.service.message;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MessageSending {

    @NotNull
    private final UUID senderId;

    @NotNull
    private final UUID receiverId;

    @NotEmpty
    private final byte[] contentForSender;

    @NotEmpty
    private final byte[] contentForReceiver;
}
