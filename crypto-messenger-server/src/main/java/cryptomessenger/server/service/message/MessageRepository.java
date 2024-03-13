package cryptomessenger.server.service.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

public interface MessageRepository extends MongoRepository<Message, UUID> {

    Page<Message> findBySenderIdOrderBySentAtDesc(UUID senderId, Pageable pageable);

    Page<Message> findByReceiverIdOrderBySentAtDesc(UUID receiverId, Pageable pageable);

    Page<Message> findBySenderIdAndReceiverIdOrderBySentAtDesc(UUID senderId, UUID receiverId, Pageable pageable);

    default Page<Message> findMessagesBetween(UUID userId, UUID otherUserId, Pageable pageable) {
        var fromUserToOtherUser = findBySenderIdAndReceiverIdOrderBySentAtDesc(userId, otherUserId, pageable);
        var fromOtherUserToUser = findBySenderIdAndReceiverIdOrderBySentAtDesc(otherUserId, userId, pageable);
        var combinedMessages = Stream.concat(fromUserToOtherUser.stream(), fromOtherUserToUser.stream())
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .toList();
        return new PageImpl<>(combinedMessages, pageable, combinedMessages.size());
    }
}
