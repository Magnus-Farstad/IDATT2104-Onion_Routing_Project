import cryptography.AESencryption;
import org.junit.jupiter.api.Assertions;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

class AESencryptionTest {

    @org.junit.jupiter.api.Test
    public void testEncryptDecrypt() throws NoSuchAlgorithmException{
        AESencryption aesEncryption = new AESencryption();

        SecretKey secretKey = aesEncryption.generateAESKey();

        String message = "This is a message";

        String encryptedMessage = aesEncryption.encrypt(message, secretKey);

        String decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);

        Assertions.assertNotEquals(message, encryptedMessage);
        Assertions.assertEquals(message, decryptedMessage);

    }

}
