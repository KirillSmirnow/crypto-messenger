package cryptomessenger.desktop.infrastructure.client.message;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "message-client", url = "${app.server-base-url}")
public interface MessageClient {

    @PostMapping("/messages")
    MessageDto send(@RequestBody MessageSendingDto sending);

    @GetMapping("/users/{userId}/inbox")
    Page<MessageDto> getInbox(@PathVariable UUID userId, Pageable pageable);

    @GetMapping("/users/{userId}/outbox")
    Page<MessageDto> getOutbox(@PathVariable UUID userId, Pageable pageable);
}
