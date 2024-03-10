package cryptomessenger.desktop.service.message;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.FormatStyle;

import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;

@Data
@Builder
public class Message {

    private final LocalDateTime sentAt;
    private final String senderUsername;
    private final String receiverUsername;
    private final String text;

    public String getSentAt() {
        return sentAt.format(ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }
}
