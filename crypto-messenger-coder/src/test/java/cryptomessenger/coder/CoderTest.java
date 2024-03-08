package cryptomessenger.coder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class CoderTest {

    @Autowired
    private Coder coder;

    @Test
    public void givenKeyPairAndMessage_whenEncodeWithPublicKeyAndDecodeWithPrivateKey_thenDecodedMessageIsEqualToOriginal() {
        var keyPair = coder.generateKeyPair();
        var originalMessage = new TextMessage("Hello there!");

        var encodedMessage = coder.encode(originalMessage, keyPair.getPublicKey());
        var decodedMessage = coder.decode(encodedMessage, keyPair.getPrivateKey());

        assertThat(decodedMessage.getText()).isEqualTo("Hello there!");
    }

    @Test
    public void givenKeyPairAndMessage_whenEncodeWithPrivateKeyAndDecodeWithPublicKey_thenDecodedMessageIsEqualToOriginal() {
        var keyPair = coder.generateKeyPair();
        var originalMessage = new TextMessage("Hello there!");

        var encodedMessage = coder.encode(originalMessage, keyPair.getPrivateKey());
        var decodedMessage = coder.decode(encodedMessage, keyPair.getPublicKey());

        assertThat(decodedMessage.getText()).isEqualTo("Hello there!");
    }

    @Test
    public void givenEncodedWithPublicKeyMessage_whenDecodeWithPublicKey_thenCoderExceptionThrown() {
        var keyPair = coder.generateKeyPair();
        var originalMessage = new TextMessage("Hello there!");
        var encodedMessage = coder.encode(originalMessage, keyPair.getPublicKey());

        assertThrows(
                CoderException.class,
                () -> coder.decode(encodedMessage, keyPair.getPublicKey())
        );
    }

    @Test
    public void givenEncodedWithPrivateKeyMessage_whenDecodeWithPrivateKey_thenCoderExceptionThrown() {
        var keyPair = coder.generateKeyPair();
        var originalMessage = new TextMessage("Hello there!");
        var encodedMessage = coder.encode(originalMessage, keyPair.getPrivateKey());

        assertThrows(
                CoderException.class,
                () -> coder.decode(encodedMessage, keyPair.getPrivateKey())
        );
    }

    @TestConfiguration
    @ComponentScan
    public static class Configuration {
    }
}
