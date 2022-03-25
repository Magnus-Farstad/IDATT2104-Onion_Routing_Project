import cryptography.AESencryption;
import cryptography.EncryptionManager;
import cryptography.RSAKeyPairGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class AESencryptionTest {


    @DisplayName("Encrypt and decrypt with AES")
    @Test
    public void encryptDecryptAES(){

        try{
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
