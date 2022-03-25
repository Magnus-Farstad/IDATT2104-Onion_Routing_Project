import cryptography.AESencryption;
import cryptography.EncryptionManager;
import cryptography.RSAKeyPairGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class AESencryptionTest {
    @Nested
    class generateKeysTest {
        @DisplayName("Successfully generate keys positive")
        @Test
        public void successfullyGenerateKeysPositive() throws NoSuchAlgorithmException {
            try {
                AESencryption aeSencryption = new AESencryption();

                String secretAESKEY = aeSencryption.convertSecretKeyToString(aeSencryption.generateAESKey());

                Assertions.assertNotNull(secretAESKEY);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        @DisplayName("Successfully generate keys negative")
        @Test
        public void successfullyGenerateKeysNegative() throws NoSuchAlgorithmException {
            try {
                AESencryption aeSencryption = new AESencryption();

                String secretAESKEY = aeSencryption.convertSecretKeyToString(aeSencryption.generateAESKey());

                Assertions.assertNull(secretAESKEY);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    @Nested
    class encryptDecryptAES {
        @DisplayName("Encrypt and decrypt with AES Positive")
        @Test
        public void encryptDecryptAESPositive() {

            try {
                AESencryption aesEncryption = new AESencryption();

                SecretKey secretKey = aesEncryption.generateAESKey();

                String message = "This is a message";

                String encryptedMessage = aesEncryption.encrypt(message, secretKey);

                String decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);

                Assertions.assertNotEquals(message, encryptedMessage);
                Assertions.assertEquals(message, decryptedMessage);

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        @DisplayName("Encrypt and decrypt with AES Negative")
        @Test
        public void encryptDecryptAESNegative() {

            try {
                AESencryption aesEncryption = new AESencryption();

                SecretKey secretKey = aesEncryption.generateAESKey();

                String message = "This is a message";

                String encryptedMessage = aesEncryption.encrypt(message, secretKey);

                String decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);

                Assertions.assertNotEquals(message, encryptedMessage);
                Assertions.assertEquals(message, decryptedMessage);

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
