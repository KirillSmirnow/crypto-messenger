package cryptomessenger.server.service.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MessageRepository extends MongoRepository<Message, UUID> {

    Page<Message> findBySenderIdOrderBySentAtDesc(UUID senderId, Pageable pageable);

    Page<Message> findByReceiverIdOrderBySentAtDesc(UUID receiverId, Pageable pageable);
}
