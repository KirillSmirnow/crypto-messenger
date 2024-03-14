package cryptomessenger.desktop.infrastructure.client.feign.dialog;

import cryptomessenger.desktop.infrastructure.client.feign.message.MessageDto;
import lombok.Data;

@Data
public class DialogMessageDto {
    private final MessageDto message;
    private final boolean isMine;
}
