package cryptomessenger.server.service.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

public interface MessageRepository extends MongoRepository<Message, UUID> {

    Page<Message> findBySenderIdOrderBySentAtDesc(UUID senderId, Pageable pageable);

    Page<Message> findByReceiverIdOrderBySentAtDesc(UUID receiverId, Pageable pageable);

    Page<Message> findBySenderIdAndReceiverIdOrderBySentAtDesc(UUID senderId, UUID receiverId, Pageable pageable);

    @Query(value = """
                { $or: [
                    { $and: [{receiverId: ?0}, {senderId: ?1}]} , { $and: [{senderId: ?0}, {receiverId: ?1}]}
                ]}
            """,
            sort = "{ sentAt : -1 }")
    Page<Message> findMessagesBetween(UUID userId, UUID otherUserId, Pageable pageable);

}
