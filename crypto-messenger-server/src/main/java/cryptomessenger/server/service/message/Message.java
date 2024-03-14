package cryptomessenger.server.service.message;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document
@EqualsAndHashCode(of = "id")
public class Message {

    private final UUID id;
    private final LocalDateTime sentAt;

    @Indexed
    private final UUID senderId;

    @Indexed
    private final UUID receiverId;

    private final byte[] contentForSender;
    private final byte[] contentForReceiver;
}
