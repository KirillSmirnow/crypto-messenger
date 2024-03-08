package cryptomessenger.desktop.service.message;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Message {
    private final LocalDateTime sentAt;
    private final String senderUsername;
    private final String receiverUsername;
    private final String text;
}
