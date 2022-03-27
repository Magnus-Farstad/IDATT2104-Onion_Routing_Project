import cryptography.AESEncryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import javax.crypto.SecretKey;

@DisplayName("Class for testing AESEncryption")
public class AESencryptionTest {

    @DisplayName("Generate AES key")
    @Nested
    class GenerateKey {

        @DisplayName("Successfully generate AES key")
        @Test
        public void successfullyGenerateKey() {

            String secretAESKey = null;
            try {
                AESEncryption aeSencryption = new AESEncryption();

                secretAESKey = aeSencryption.convertSecretKeyToString(aeSencryption.generateAESKey());

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            finally {
                Assertions.assertNotNull(secretAESKey);
            }
        }
    }

    @DisplayName("Tests for encrypting and decrypting with AES key")
    @Nested
    class EncryptDecryptAES {

        @DisplayName("Successfully encrypt and decrypt with AES")
        @Test
        public void successfullyEncryptDecryptAES() {

            String message = null;
            String decryptedMessage = "";
            String encryptedMessage = null;

            try {
                AESEncryption aesEncryption = new AESEncryption();
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

        @DisplayName("Unsuccessfully encrypt and decrypt with AES")
        @Test
        public void UnsuccessfullyEncryptDecryptAES() {

            String message = null;
            String decryptedMessage = "";
            String encryptedMessage = null;
            try {
                AESEncryption aesEncryption = new AESEncryption();
                SecretKey secretKey = aesEncryption.generateAESKey();
                SecretKey secretKey1 = aesEncryption.generateAESKey();

                message = "This is a message";
                encryptedMessage = aesEncryption.encrypt(message, secretKey);
                decryptedMessage = aesEncryption.decrypt(encryptedMessage, secretKey1);

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            finally {
                Assertions.assertNotEquals(message, encryptedMessage);
                Assertions.assertNotEquals(message, decryptedMessage);
            }
        }
    }
}
