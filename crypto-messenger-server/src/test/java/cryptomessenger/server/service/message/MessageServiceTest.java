package cryptomessenger.server.service.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataMongoTest
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        messageRepository.deleteAll();
    }

    @Test
    public void givenMessageSending_whenSend_thenMessageSavedCorrectly() {
        var sending = MessageSending.builder()
                .senderId(randomUUID())
                .receiverId(randomUUID())
                .contentForSender(newContent())
                .contentForReceiver(newContent())
                .build();

        var returnedMessage = messageService.send(sending);

        var savedMessage = messageRepository.findById(returnedMessage.getId());
        assertThat(savedMessage).isPresent();
        assertThat(savedMessage.get().getId()).isNotNull();
        assertThat(savedMessage.get().getSentAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(savedMessage.get().getSenderId()).isEqualTo(sending.getSenderId());
        assertThat(savedMessage.get().getReceiverId()).isEqualTo(sending.getReceiverId());
        assertThat(savedMessage.get().getContentForSender()).isEqualTo(sending.getContentForSender());
        assertThat(savedMessage.get().getContentForReceiver()).isEqualTo(sending.getContentForReceiver());
    }

    @Test
    public void givenUserAndMessages_whenGetInbox_thenReturnMessagesSentToUser() {
        var userId = randomUUID();
        saveMessageSentTo(userId);
        saveMessageSentTo(userId);
        saveMessageSentTo(randomUUID());

        var inbox = messageService.getInbox(userId, Pageable.unpaged());

        assertThat(inbox).hasSize(2);
        assertThat(inbox).allMatch(message -> message.getReceiverId().equals(userId));
    }

    @Test
    public void givenUserAndMessages_whenGetOutbox_thenReturnMessagesSentByUser() {
        var userId = randomUUID();
        saveMessageSentBy(userId);
        saveMessageSentBy(userId);
        saveMessageSentBy(randomUUID());

        var outbox = messageService.getOutbox(userId, Pageable.unpaged());

        assertThat(outbox).hasSize(2);
        assertThat(outbox).allMatch(message -> message.getSenderId().equals(userId));
    }

    private void saveMessageSentTo(UUID receiverId) {
        messageRepository.save(
                Message.builder()
                        .id(randomUUID())
                        .receiverId(receiverId)
                        .build()
        );
    }

    private void saveMessageSentBy(UUID senderId) {
        messageRepository.save(
                Message.builder()
                        .id(randomUUID())
                        .senderId(senderId)
                        .build()
        );
    }

    private byte[] newContent() {
        var key = new byte[100];
        ThreadLocalRandom.current().nextBytes(key);
        return key;
    }

    @TestConfiguration
    @ComponentScan
    public static class Configuration {
    }
}
