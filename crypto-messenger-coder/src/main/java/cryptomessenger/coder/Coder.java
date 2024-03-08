package cryptomessenger.coder;

import lombok.Builder;
import lombok.Data;

public interface Coder {

    Message encode(Message message, Key key);

    Message decode(Message message, Key key);

    KeyPair generateKeyPair();

    @Data
    @Builder
    class KeyPair {
        private final Key privateKey;
        private final Key publicKey;
    }
}
