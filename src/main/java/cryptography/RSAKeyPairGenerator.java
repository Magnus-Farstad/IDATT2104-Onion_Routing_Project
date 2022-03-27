package cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Class for generating RSA public and private key, as well ass encrypting and decrypting with this key pair.
 */

public class RSAKeyPairGenerator {


    private PrivateKey privateKey;
    private PublicKey publicKey;


    /**
     * Constructor for the class
     * @throws NoSuchAlgorithmException
     */

   public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
       KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
       keyGen.initialize(1024);
       KeyPair pair = keyGen.generateKeyPair();
       this.privateKey = pair.getPrivate();
       this.publicKey = pair.getPublic();

   }

   /**
     * A method for generating a pair of RSA keys - public and private
     * It uses classes and methods from the javax.crypto package.
     * @throws NoSuchAlgorithmException
     */
    public void initKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    /**
     * A method for generating public and private key pair from String format
     * Uses encoding and keyFactory to do so
     * @param PUBLIC_KEY public key that is being generated
     * @param PRIVATE_KEY private key that is being generated
     */

    public void initFromStrings(String PUBLIC_KEY, String PRIVATE_KEY ){
        try{
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY));
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        }catch (Exception ignored){}
    }

    /**
     * A method to transform bytes array to an encoded String
     * Using Base64
     * @param data the data that is being encoded
     * @return returns the encoded data
     */

    public String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * A method for decoding a String
     * Using Base64
     * @param data
     * @return returns a decoded String
     */

    public byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * A method for encrypting a String
     * Uses a generated public RSA key to encrypt
     * Uses the method "encode" to transform the encrypted byte message to a String
     * @param data the data that is being encrypted
     * @return returns the encrypted message
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

    /**
     * A method that decrypts an encrypted String
     * Uses the method "decode" to decode a string to bytes
     * @param encryptedMessage
     * @return returns a decrypted String
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */

    public String decrypt(String encryptedMessage) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(decode(encryptedMessage));
        return new String(decryptedMessage, "UTF8");
    }

    /**
     * A method that prints out the RSA key pair to terminal
     */

    public void printKeys() {
        System.out.println("\nPublic key:" + encode(publicKey.getEncoded()));
        System.out.println("Private key: " + encode(privateKey.getEncoded()));
    }


    /**
     * A method that retrieves the private key
     * @return returns private key
     */
    public String getPrivateKey() {
        return encode(privateKey.getEncoded());
    }

    /**
     * A method that retrieves the public key
     * @return returns public key
     */
    public String getPublicKey() {
        return encode(publicKey.getEncoded());
    }

    /**
     * A main method for testing purposes
     * @param args
     * @throws NoSuchAlgorithmException
     */

    public static void main(String[] args) throws NoSuchAlgorithmException{
        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();

        keyPairGenerator.initKeys();
        keyPairGenerator.printKeys();
    }
}
