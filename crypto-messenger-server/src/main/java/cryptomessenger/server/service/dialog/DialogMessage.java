package cryptomessenger.server.service.dialog;

import cryptomessenger.server.service.message.Message;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DialogMessage {
    private final Message message;
    private final boolean mine;
}
