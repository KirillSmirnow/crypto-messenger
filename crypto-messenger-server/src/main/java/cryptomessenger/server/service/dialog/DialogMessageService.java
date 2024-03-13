package cryptomessenger.server.service.dialog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DialogMessageService {
    Page<DialogMessage> getDialogMessagesFor(UUID myId, UUID otherUserId, Pageable pageable);
}
