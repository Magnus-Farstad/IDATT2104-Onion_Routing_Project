import cryptography.AESencryption;
import cryptography.EncryptionManager;
import cryptography.RSAKeyPairGenerator;
import org.junit.jupiter.api.Assertions;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class AESencryptionTest {

    @org.junit.jupiter.api.Test
    public void testEncryptDecryptAES() throws NoSuchAlgorithmException{
        AESencryption aesEncryption = new AESencryption();

        SecretKey secretKey = aesEncryption.generateAESKey();

        String message = "This is a message";

        String encryptedMessage = aesEncryption.encrypt(message, secretKey);

        String decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);

        Assertions.assertNotEquals(message, encryptedMessage);
        Assertions.assertEquals(message, decryptedMessage);
    }
}
