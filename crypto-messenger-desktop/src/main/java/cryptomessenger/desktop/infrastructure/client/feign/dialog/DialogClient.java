package cryptomessenger.desktop.infrastructure.client.feign.dialog;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "dialog-client", url = "${app.server-base-url}")
public interface DialogClient {

    @GetMapping("/dialogs/{myId}/{otherUserId}")
    Page<DialogMessageDto> getDialog(@PathVariable UUID myId, @PathVariable UUID otherUserId, Pageable pageable);
}
