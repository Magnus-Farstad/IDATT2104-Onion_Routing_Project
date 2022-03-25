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
        public void successfullyGenerateKeysPositive() {

            String secretAESKEY = null;
            try {
                AESencryption aeSencryption = new AESencryption();

                secretAESKEY = aeSencryption.convertSecretKeyToString(aeSencryption.generateAESKey());


            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            } finally {
                Assertions.assertNotNull(secretAESKEY);
            }
        }

        @DisplayName("Successfully generate keys negative")
        @Test
        public void successfullyGenerateKeysNegative() {

            String secretAESKEY = null;
            try {
                AESencryption aeSencryption = new AESencryption();

                secretAESKEY = aeSencryption.convertSecretKeyToString(aeSencryption.generateAESKey());

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            } finally {
                Assertions.assertNull(secretAESKEY);
            }
        }
    }

    @Nested
    class encryptDecryptAES {
        @DisplayName("Encrypt and decrypt with AES Positive")
        @Test
        public void encryptDecryptAESPositive() {

            String message = null;
            String decryptedMessage = "";
            String encryptedMessage = null;

            try {
                AESencryption aesEncryption = new AESencryption();

                SecretKey secretKey = aesEncryption.generateAESKey();

                message = "This is a message";

                encryptedMessage = aesEncryption.encrypt(message, secretKey);

                decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);



            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            finally {
                Assertions.assertNotEquals(message, encryptedMessage);
                Assertions.assertEquals(message, decryptedMessage);
            }
        }

        @DisplayName("Encrypt and decrypt with AES Negative")
        @Test
        public void encryptDecryptAESNegative() { //Denne er enda ikke gjort negativ!

            String message = null;
            String decryptedMessage = "";
            String encryptedMessage = null;

            try {
                AESencryption aesEncryption = new AESencryption();

                SecretKey secretKey = aesEncryption.generateAESKey();

                message = "This is a message";

                encryptedMessage = aesEncryption.encrypt(message, secretKey);

                decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey);

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            finally {
                Assertions.assertNotEquals(message, encryptedMessage);
                Assertions.assertEquals(message, decryptedMessage);
            }
        }
    }
}
