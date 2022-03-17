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

public class RSAUtil1 {

    private PrivateKey privateKey;
    private final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs6hMk1chReRa76VAVx42YDGAv+bGwLlxAZybLwA9pFuU2oj1T9pdDWQen4m/Yo0oOFQAKsd1FWnILPtdIU3Is++k4DYMCSEQK+FA4a1wZ5ghx1QiUcu/e08gl9nDtYyGZixZpGb8hM5PToyNTL1AqKDEPOvEEX+Lia9Hp9/7hVAgMBAAECgYAEmiSLrpj2P6FDQcqx6qF6Sccxu9ZNEb7lzE3tjy4eD4kh40h0lZyP53pGzIcbMjSjj5TJP4G+GJpFSpEybEnpjxPIljg42pEJNvpKLxbT86ZCM0WN4qfXL5dA5sWqY+laB59Td+fATnc1Nj0n8CL3/i6PpQwg01M5qoJTPgVZAQJBANmfWXcX88s+XuFB7hFjLGi0euCvMW/dXeETWk1CYQEkMGEG5+ElrGiLFiraO6CIdZ1nOQueD7RQtM5846taKMECQQC2ml+qFhCGzez8HK1cMXEgMNzM3aLhvUmp6fNNhqDSNSYzGy1K6NiJX1CE9yOa525M4nZ1Jh1vPnrws3BOBgCVAkEAmqCUywAj45fPhrJ327bhyQvj+12//MIHgHNlyFuP3WW/UlG71MgV9rpM5+nkUC5lk4/SgqSud+qYbddjVU9cgQJAEbh5gDAT+oERdoXx7Ph/Wfhj9R2tKOsNsweZLPTbtoqh4mPIyXQ/T1WIot64/ddnxN5VUJkaUilmFOXVCD1c4QJBAIaZyp6Q8HIyL7iwClaCAfu8mSt+aB+9s4avPzexFJrvfogunGaU62F6oV78LRsiDbe+6sjG0nGqnWDvkrSDpH0=";

    public void initFromStrings(){
        try{
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        }catch (Exception ignored){}
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String decrypt(String encryptedMessage) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(decode(encryptedMessage));
        return new String(decryptedMessage, "UTF8");
    }
}