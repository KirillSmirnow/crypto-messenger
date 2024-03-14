package cryptomessenger.server.service.dialog;

import cryptomessenger.server.service.message.Message;
import cryptomessenger.server.service.message.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class DialogMessageServiceTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private DialogMessageService dialogMessageService;

    @BeforeEach
    public void setUp() {
        messageRepository.deleteAll();
    }

    @Test
    public void givenMessages_whenGetDialogMessages_thenReturnsCorrectDialog() {
        var userId = randomUUID();
        var otherUserId = randomUUID();
        var message1 = saveMessage(userId, otherUserId);
        var message2 = saveMessage(otherUserId, userId);
        saveMessage(randomUUID(), randomUUID());

        var dialog = dialogMessageService.getDialogMessagesFor(userId, otherUserId, Pageable.unpaged());

        assertThat(dialog).hasSize(2);
        assertThat(dialog.getContent().get(0).getMessage()).isEqualTo(message2);
        assertThat(dialog.getContent().get(1).getMessage()).isEqualTo(message1);

        assertThat(dialog.getContent().get(0).isMine()).isFalse();
        assertThat(dialog.getContent().get(1).isMine()).isTrue();
    }

    @Test
    public void givenMessageFromUserToHimSelf_whenGetDialogMessages_thenReturnsOnlyOneDialogMessage() {
        var userId = randomUUID();
        saveMessage(userId, userId);
        saveMessage(randomUUID(), randomUUID());

        var dialog = dialogMessageService.getDialogMessagesFor(userId, userId, Pageable.unpaged());

        assertThat(dialog).hasSize(1);
        assertThat(dialog.getContent().get(0).isMine()).isTrue();
    }

    private Message saveMessage(UUID senderId, UUID receiverId) {
        return messageRepository.save(
                Message.builder()
                        .id(randomUUID())
                        .senderId(senderId)
                        .receiverId(receiverId)
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }

    @TestConfiguration
    @ComponentScan
    public static class Configuration {
    }
}
