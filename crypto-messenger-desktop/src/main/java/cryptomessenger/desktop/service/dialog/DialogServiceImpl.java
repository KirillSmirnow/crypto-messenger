package cryptomessenger.desktop.service.dialog;

import cryptomessenger.coder.Coder;
import cryptomessenger.desktop.infrastructure.client.feign.dialog.DialogClient;
import cryptomessenger.desktop.infrastructure.client.feign.dialog.DialogMessageDto;
import cryptomessenger.desktop.infrastructure.client.feign.message.MessageDto;
import cryptomessenger.desktop.infrastructure.client.feign.user.UserClient;
import cryptomessenger.desktop.infrastructure.client.feign.user.UserDto;
import cryptomessenger.desktop.infrastructure.localstorage.LocalStorage;
import cryptomessenger.desktop.service.LocalStorageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

import static java.time.ZoneOffset.UTC;

@Service
@RequiredArgsConstructor
public class DialogServiceImpl implements DialogService {

    private final LocalStorage localStorage;
    private final UserClient userClient;
    private final DialogClient dialogClient;
    private final Coder coder;

    @Override
    public Page<DialogMessage> getMessages(String username, Pageable pageable) {
        var currentUser = getCurrentUser();
        var receiver = userClient.getByUsername(username);
        try {
            return dialogClient.getDialog(currentUser.getId(), receiver.getId(), pageable)
                    .map(this::convertDialogMessage);
        } catch (Exception e) {
            return Page.empty();
        }
    }

    private UserDto getCurrentUser() {
        var currentUsername = localStorage.getString(LocalStorageKeys.CURRENT_USERNAME);
        return userClient.getByUsername(currentUsername);
    }

    private DialogMessage convertDialogMessage(DialogMessageDto dto) {
        var sentAt = dto.getMessage()
                .getSentAt()
                .atZone(UTC)
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
        var isMine = dto.isMine();
        return DialogMessage.builder()
                .sentAt(sentAt)
                .outbox(isMine ? decodeText(dto.getMessage()) : "")
                .inbox(!isMine ? decodeText(dto.getMessage()) : "")
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
