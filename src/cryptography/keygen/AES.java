package cryptography.keygen;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {

    private SecretKey key;
    private byte[] IV;
    /**
     * Possible AES KEY_SIZE values are 128, 192 and 256
     * Possible tag length (T_LEN) values are 128, 120, 112, 104 and 96
     */
    private final int KEY_SIZE = 128;
    private final int T_LEN = 128;

    public void init() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
    }

    public void initFromStrings(String secretKey, String IV) {
        this.key = new SecretKeySpec(decode(secretKey), "AES");
        this.IV = decode(IV);
    }

    private String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }

    public byte[] encryptBytes(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
        IV = encryptionCipher.getIV();
        return encryptionCipher.doFinal(data);
    }

    public byte[] decryptBytes(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE,key,spec);
        return decryptionCipher.doFinal(data);
    }

    public String encryptOld(String message) throws Exception{
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE,key);
        IV = encryptionCipher.getIV();
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String encrypt(String message) throws Exception{
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
        IV = encryptionCipher.getIV();
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception{
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE,key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    private void exportKeys() {
        System.out.println("SecretKey: " + encode(key.getEncoded()));
        System.out.println("IV: " + encode(IV));
    }

    public static void main(String[] args) throws Exception {

        AES aes = new AES();
        aes.initFromStrings("o42ao5pv1TEoTsQ3rN1ASg==", "jpckxf77xxMcS51A");
        String encryptedMessage = aes.encrypt("TheXCoders");
        String decryptedMessage = aes.decrypt(encryptedMessage);

        System.out.println("Encrypted Message : " + encryptedMessage);
        System.out.println("Decrypted Message : " + decryptedMessage);
        System.out.println();
    }

}
