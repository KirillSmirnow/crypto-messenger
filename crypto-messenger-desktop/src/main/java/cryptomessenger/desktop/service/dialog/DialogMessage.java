package cryptomessenger.desktop.service.dialog;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.FormatStyle;

import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;

@Data
@Builder
public class DialogMessage {

    private final LocalDateTime sentAt;
    private String inbox;
    private String outbox;

    public String getSentAt() {
        return sentAt.format(ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }
}
