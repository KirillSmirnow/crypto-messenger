package cryptomessenger.server.infrastructure.controller;

import cryptomessenger.server.service.dialog.DialogMessage;
import cryptomessenger.server.service.dialog.DialogMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/dialogs")
@RequiredArgsConstructor
public class DialogMessageController {

    private final DialogMessageService dialogMessageService;

    @GetMapping("/{myId}/{otherUserId}")
    public Page<DialogMessage> getDialogMessage(
            @PathVariable UUID myId,
            @PathVariable UUID otherUserId,
            Pageable pageable
    ) {
        return dialogMessageService.getDialogMessagesFor(myId, otherUserId, pageable);
    }
}
