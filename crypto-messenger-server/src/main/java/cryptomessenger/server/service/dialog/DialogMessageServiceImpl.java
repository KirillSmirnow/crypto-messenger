package cryptomessenger.server.service.dialog;

import cryptomessenger.server.service.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DialogMessageServiceImpl implements DialogMessageService {

    private final MessageRepository messageRepository;

    @Override
    public Page<DialogMessage> getDialogMessagesFor(UUID myId, UUID otherUserId, Pageable pageable) {
        var messages = messageRepository.findMessagesBetween(myId, otherUserId, pageable);
        var dialogs = messages.stream()
                .map(message -> DialogMessage.builder()
                        .message(message)
                        .isMine(message.getSenderId().equals(myId))
                        .build())
                .toList();
        return new PageImpl<>(dialogs, pageable, dialogs.size());
    }
}
