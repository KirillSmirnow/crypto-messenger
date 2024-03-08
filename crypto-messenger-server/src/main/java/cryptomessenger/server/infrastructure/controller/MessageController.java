package cryptomessenger.server.infrastructure.controller;

import cryptomessenger.server.service.message.Message;
import cryptomessenger.server.service.message.MessageSending;
import cryptomessenger.server.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/messages")
    public Message send(@RequestBody MessageSending sending) {
        return messageService.send(sending);
    }

    @GetMapping("/users/{userId}/inbox")
    public Page<Message> getInbox(@PathVariable UUID userId, Pageable pageable) {
        return messageService.getInbox(userId, pageable);
    }

    @GetMapping("/users/{userId}/outbox")
    public Page<Message> getOutbox(@PathVariable UUID userId, Pageable pageable) {
        return messageService.getOutbox(userId, pageable);
    }
}
