package cryptomessenger.server.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message send(MessageSending sending) {
        var message = Message.builder()
                .id(randomUUID())
                .sentAt(LocalDateTime.now())
                .senderId(sending.getSenderId())
                .receiverId(sending.getReceiverId())
                .contentForSender(sending.getContentForSender())
                .contentForReceiver(sending.getContentForReceiver())
                .build();
        return messageRepository.save(message);
    }

    @Override
    public Page<Message> getInbox(UUID userId, Pageable pageable) {
        return messageRepository.findByReceiverIdOrderBySentAtDesc(userId, pageable);
    }

    @Override
    public Page<Message> getOutbox(UUID userId, Pageable pageable) {
        return messageRepository.findBySenderIdOrderBySentAtDesc(userId, pageable);
    }
}
