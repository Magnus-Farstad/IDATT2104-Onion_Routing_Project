package cryptography;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class for generating AES key, encrypting and decrypting with this key.
 * Methods in this class utilizes packages from the javax.crypto package, Base64 package and the java.security package.
 */
public class AESEncryption {

    /**
     * Method for generating AES key.
     * It uses classes and methods from the javax.crypto package.
     * @return The generated key
     * @throws NoSuchAlgorithmException
     */
    public SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        return  kgen.generateKey();
    }

    /**
     * Method for encrypting a String to another encrypted String.
     * This method uses a generated key to encrypt.
     * @param string The String to be encrypted
     * @param key The key to encrypt with
     * @return The encrypted String
     */
    public String encrypt(final String string, final SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    /**
     * Method for converting a secret key from SecretKey to a String.
     * @param secretKey The SecretKey to be converted to String
     * @return The String of a secret key
     * @throws NoSuchAlgorithmException
     */
    public String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    /**
     * Method for converting String to SecretKey.
     * @param encodedKey A String of a secret key
     * @return The SecretKey converted from a String
     */
    public SecretKey convertStringToSecretKeyto(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }

    /**
     * Method for decrypting a String with an AES secret key.
     * This method uses the key that encrypted the message, to decrypt the message
     * @param string The message String to be decrypted
     * @param key The SecretKey secret key to decrypt with
     * @return The decrypted String message
     */
    public String decrypt(final String string, final SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(string)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }
}
