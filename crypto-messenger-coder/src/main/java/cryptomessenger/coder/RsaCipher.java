package cryptomessenger.coder;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
class RsaCipher implements Coder {

    private final Cipher cipher;
    private final KeyFactory keyFactory;
    private final KeyPairGenerator keyPairGenerator;

    @SneakyThrows
    public RsaCipher() {
        var algorithm = "RSA";
        cipher = Cipher.getInstance(algorithm);
        keyFactory = KeyFactory.getInstance(algorithm);
        keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
    }

    @Override
    public Message encode(Message message, Key key) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, normalizeKey(key));
            var result = cipher.doFinal(message.getBytes());
            return () -> result;
        } catch (Exception cause) {
            throw new CoderException(cause);
        }
    }

    @Override
    public Message decode(Message message, Key key) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, normalizeKey(key));
            var result = cipher.doFinal(message.getBytes());
            return () -> result;
        } catch (Exception cause) {
            throw new CoderException(cause);
        }
    }

    @SneakyThrows
    private java.security.Key normalizeKey(Key key) {
        try {
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(key.getBytes()));
        } catch (InvalidKeySpecException e) {
            return keyFactory.generatePublic(new X509EncodedKeySpec(key.getBytes()));
        }
    }

    @Override
    public KeyPair generateKeyPair() {
        var keyPair = keyPairGenerator.generateKeyPair();
        return KeyPair.builder()
                .privateKey(() -> keyPair.getPrivate().getEncoded())
                .publicKey(() -> keyPair.getPublic().getEncoded())
                .build();
    }
}
