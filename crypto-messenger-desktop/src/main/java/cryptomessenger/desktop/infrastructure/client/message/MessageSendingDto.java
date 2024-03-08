package cryptomessenger.desktop.infrastructure.client.message;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MessageSendingDto {
    private final UUID senderId;
    private final UUID receiverId;
    private final byte[] contentForSender;
    private final byte[] contentForReceiver;
}
