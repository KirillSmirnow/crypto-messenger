package cryptomessenger.desktop.service.dialog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DialogService {
    Page<DialogMessage> getMessages(String username, Pageable pageable);
}
