package cryptomessenger.server.service.message;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface MessageService {

    Message send(@Valid MessageSending sending);

    Page<Message> getInbox(UUID userId, Pageable pageable);

    Page<Message> getOutbox(UUID userId, Pageable pageable);
}
