package cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Class for initializing the public key from the client, and encrypting
 * This class utilizes packages from javax.crypto, Base64 and java.security.
 */
public class EncryptionManager {

    private PublicKey publicKey;
    String PUBLIC_KEY;

    /**
     * Constructor setting the provided public key String, received from the client.
     * @param PUBLIC_KEY The public key as a String
     */
    public EncryptionManager(String PUBLIC_KEY){
        this.PUBLIC_KEY = PUBLIC_KEY;
    }

    /**
     * Method for initializing the public key, converting it to a PublicKey object.
     */
    public void initFromStrings(){
        try{
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
        }catch (Exception ignored){}
    }

    /**
     * Helper method for encoding a byte array to a String
     * @param data The byte array to be encoded
     * @return The String encoded with Base64
     */
    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Helper method for decoding a String to a byte array
     * @param data The String to be decoded
     * @return The byte array decoded with Base64
     */
    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * Method for encrypting a String with an RSA PublicKey.
     * @param data The String message to be encrypted
     * @return The encrypted String
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public String encrypt(String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedMessage = cipher.doFinal(data.getBytes());
        return encode(encryptedMessage);
    }


}