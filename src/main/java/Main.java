import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Main {

    private static final String ALGORITHM = "RSA";

    public static void main(String[] args) throws Exception {
        // generateKeyPair();

        var message = "Dear Mr Capuletti,";

        var cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        var encodedMessage = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        System.out.println(Base64.getEncoder().encodeToString(encodedMessage));

        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        var decodedMessage = cipher.doFinal(encodedMessage);
        System.out.println(new String(decodedMessage, StandardCharsets.UTF_8));
    }

    private static void generateKeyPair() throws Exception {
        var generator = KeyPairGenerator.getInstance(ALGORITHM);
        var keyPair = generator.generateKeyPair();
        Files.write(Path.of("./private-key"), keyPair.getPrivate().getEncoded());
        Files.write(Path.of("./public-key"), keyPair.getPublic().getEncoded());
    }

    private static Key getPrivateKey() throws Exception {
        var keyFactory = KeyFactory.getInstance(ALGORITHM);
        var keySpec = new PKCS8EncodedKeySpec(Files.readAllBytes(Path.of("./private-key")), ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    private static Key getPublicKey() throws Exception {
        var keyFactory = KeyFactory.getInstance(ALGORITHM);
        var keySpec = new X509EncodedKeySpec(Files.readAllBytes(Path.of("./public-key")), ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }
}
