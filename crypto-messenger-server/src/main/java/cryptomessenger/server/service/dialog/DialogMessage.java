package cryptomessenger.server.service.dialog;

import com.fasterxml.jackson.annotation.JsonProperty;
import cryptomessenger.server.service.message.Message;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DialogMessage {
    private final Message message;

    @JsonProperty("isMine")
    private final boolean isMine;
}
