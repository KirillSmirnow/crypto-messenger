package cryptomessenger.desktop.service.message;

import cryptomessenger.coder.Coder;
import cryptomessenger.desktop.infrastructure.client.message.MessageClient;
import cryptomessenger.desktop.infrastructure.client.message.MessageDto;
import cryptomessenger.desktop.infrastructure.client.message.MessageSendingDto;
import cryptomessenger.desktop.infrastructure.client.user.UserClient;
import cryptomessenger.desktop.infrastructure.client.user.UserDto;
import cryptomessenger.desktop.infrastructure.localstorage.LocalStorage;
import cryptomessenger.desktop.service.LocalStorageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageClient messageClient;
    private final UserClient userClient;
    private final LocalStorage localStorage;
    private final Coder coder;

    @Override
    public void send(String receiverUsername, String text) {
        var sender = getCurrentUser();
        var receiver = userClient.getByUsername(receiverUsername);
        messageClient.send(
                MessageSendingDto.builder()
                        .senderId(sender.getId())
                        .receiverId(receiver.getId())
                        .contentForSender(coder.encode(text::getBytes, sender::getPublicKey).getBytes())
                        .contentForReceiver(coder.encode(text::getBytes, receiver::getPublicKey).getBytes())
                        .build()
        );
    }

    private UserDto getCurrentUser() {
        var currentUsername = localStorage.getString(LocalStorageKeys.CURRENT_USERNAME);
        return userClient.getByUsername(currentUsername);
    }

    @Override
    public Page<Message> getInbox(Pageable pageable) {
        try {
            var currentUser = getCurrentUser();
            return messageClient.getInbox(currentUser.getId(), pageable).map(this::convertMessage);
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Message> getOutbox(Pageable pageable) {
        try {
            var currentUser = getCurrentUser();
            return messageClient.getOutbox(currentUser.getId(), pageable).map(this::convertMessage);
        } catch (Exception e) {
            return Page.empty();
        }
    }

    private Message convertMessage(MessageDto dto) {
        var sender = userClient.getById(dto.getSenderId());
        var receiver = userClient.getById(dto.getReceiverId());
        return Message.builder()
                .sentAt(dto.getSentAt())
                .senderUsername(sender.getUsername())
                .receiverUsername(receiver.getUsername())
                .text(decodeText(dto))
                .build();
    }

    private String decodeText(MessageDto dto) {
        var currentUser = getCurrentUser();
        var isCurrentUserSender = currentUser.getId().equals(dto.getSenderId());
        var privateKey = localStorage.getBytes(LocalStorageKeys.PRIVATE_KEY);
        try {
            return coder.decode(
                    () -> isCurrentUserSender ? dto.getContentForSender() : dto.getContentForReceiver(),
                    () -> privateKey
            ).getText();
        } catch (Exception e) {
            return "[Unable to decode]";
        }
    }
}
