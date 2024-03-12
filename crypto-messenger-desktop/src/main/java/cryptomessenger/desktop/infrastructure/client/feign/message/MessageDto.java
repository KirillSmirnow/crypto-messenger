package cryptomessenger.desktop.infrastructure.client.feign.message;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDto {
    private final UUID id;
    private final LocalDateTime sentAt;
    private final UUID senderId;
    private final UUID receiverId;
    private final byte[] contentForSender;
    private final byte[] contentForReceiver;
}
