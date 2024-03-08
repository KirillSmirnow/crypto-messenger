package cryptomessenger.desktop.service.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    void send(String receiverUsername, String text);

    Page<Message> getInbox(Pageable pageable);

    Page<Message> getOutbox(Pageable pageable);
}
