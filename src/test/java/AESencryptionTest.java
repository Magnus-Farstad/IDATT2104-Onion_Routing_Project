import cryptography.AESEncryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import javax.crypto.SecretKey;

@DisplayName("Class for testing AESEncryption")
public class AESencryptionTest {

    @DisplayName("")
    @Nested
    class generateKeysTest {

        @DisplayName("Successfully generate keys")
        @Test
        public void successfullyGenerateKeys() {
            String secretAESkey = null;
            try {
                AESEncryption aeSencryption = new AESEncryption();

                secretAESkey = aeSencryption.convertSecretKeyToString(aeSencryption.generateAESKey());


            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            finally {
                Assertions.assertNotNull(secretAESkey);
            }
        }
    }

    @Nested
    class encryptDecryptAES {
        @DisplayName("Encrypt and decrypt with AES")
        @Test
        public void encryptDecryptAESPositive() {

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

        @DisplayName("Encrypt and decrypt with AES Negative")
        @Test
        public void encryptDecryptAESNegative() { //Denne er enda ikke gjort negativ!

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
