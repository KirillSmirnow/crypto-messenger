package cryptomessenger.server.infrastructure.websocket;

import cryptomessenger.server.service.message.Message;
import cryptomessenger.server.service.message.NewMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewMessageNotifier implements NewMessageListener {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onNewMessage(Message message) {
        var receiver = message.getReceiverId().toString();
        simpMessagingTemplate.convertAndSendToUser(receiver, "/messages/new", "New message");
    }
}
