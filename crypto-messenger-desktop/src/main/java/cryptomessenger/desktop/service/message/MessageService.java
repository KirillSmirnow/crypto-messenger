package cryptomessenger.desktop.service.message;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

@Validated
public interface MessageService {

    void send(String receiverUsername, @NotBlank String text);

    Page<Message> getInbox(Pageable pageable);

    Page<Message> getOutbox(Pageable pageable);
}
