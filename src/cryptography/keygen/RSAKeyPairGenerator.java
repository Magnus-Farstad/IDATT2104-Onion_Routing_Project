package cryptography.keygen;

import model.Payload;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyPairGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    // For første ledd i kommunikasjon fra klient til første node
    private final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs6hMk1chReRa76VAVx42YDGAv+bGwLlxAZybLwA9pFuU2oj1T9pdDWQen4m/Yo0oOFQAKsd1FWnILPtdIU3Is++k4DYMCSEQK+FA4a1wZ5ghx1QiUcu/e08gl9nDtYyGZixZpGb8hM5PToyNTL1AqKDEPOvEEX+Lia9Hp9/7hVAgMBAAECgYAEmiSLrpj2P6FDQcqx6qF6Sccxu9ZNEb7lzE3tjy4eD4kh40h0lZyP53pGzIcbMjSjj5TJP4G+GJpFSpEybEnpjxPIljg42pEJNvpKLxbT86ZCM0WN4qfXL5dA5sWqY+laB59Td+fATnc1Nj0n8CL3/i6PpQwg01M5qoJTPgVZAQJBANmfWXcX88s+XuFB7hFjLGi0euCvMW/dXeETWk1CYQEkMGEG5+ElrGiLFiraO6CIdZ1nOQueD7RQtM5846taKMECQQC2ml+qFhCGzez8HK1cMXEgMNzM3aLhvUmp6fNNhqDSNSYzGy1K6NiJX1CE9yOa525M4nZ1Jh1vPnrws3BOBgCVAkEAmqCUywAj45fPhrJ327bhyQvj+12//MIHgHNlyFuP3WW/UlG71MgV9rpM5+nkUC5lk4/SgqSud+qYbddjVU9cgQJAEbh5gDAT+oERdoXx7Ph/Wfhj9R2tKOsNsweZLPTbtoqh4mPIyXQ/T1WIot64/ddnxN5VUJkaUilmFOXVCD1c4QJBAIaZyp6Q8HIyL7iwClaCAfu8mSt+aB+9s4avPzexFJrvfogunGaU62F6oV78LRsiDbe+6sjG0nGqnWDvkrSDpH0=";
    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";

    // For andre ledd i kommunikasjon fra første til andre node

    // For andre ledd i kommunikasjon fra andre til tredje node


//    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(1024);
//        KeyPair pair = keyGen.generateKeyPair();
//        this.privateKey = pair.getPrivate();
//        this.publicKey = pair.getPublic();
//    }

    // Ikke kjør denne!!
    public void initKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public void initFromStrings(){
        try{
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY));
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        }catch (Exception ignored){}
    }

    public String encrypt(String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedMessage = cipher.doFinal(data.getBytes());
        return encode(encryptedMessage);
    }

//    public byte[] encryptObject(Payload payload) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
//
//            objectOutputStream.writeObject(payload);
//            System.out.println(outputStream.toByteArray().length);
//            return cipher.doFinal(outputStream.toByteArray());
//        } catch (Exception exception) {
//            System.out.println("Kryptering av objekt feilet");
//            exception.printStackTrace();
//            return null;
//        }
//    }

    public byte [] serializeObject(Payload payload) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(payload);
            System.out.println();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception exception) {
            System.out.println("Kryptering av objekt feilet");
            exception.printStackTrace();
        }
        throw new RuntimeException("Objekt til bytes feilet");
    }

    public Payload deserializeObject(byte[] bytes) {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (Payload) objectInputStream.readObject();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        throw new RuntimeException("Bytes til objekt feilet");
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public String decrypt(String encryptedMessage) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(decode(encryptedMessage));
        return new String(decryptedMessage, "UTF8");
    }

//    public Payload decryptObject(byte[] encryptedMessage) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
//
//        InputStream inputStream = new ByteArrayInputStream(decryptedBytes);
//        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
//            return (Payload) objectInputStream.readObject();
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//        }
//        throw new RuntimeException("Decryption failed");
//    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public void printKeys() {
        System.out.println("\nPublic key:" + encode(publicKey.getEncoded()));
        System.out.println("Private key: " + encode(privateKey.getEncoded()));
    }

//    public void writeToFile(String path, byte[] key) throws IOException {
//        File f = new File(path);
//        f.getParentFile().mkdirs();
//
//        FileOutputStream fos = new FileOutputStream(f);
//        fos.write(key);
//        fos.flush();
//        fos.close();
//    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        keyPairGenerator.initFromStrings();
        String message = "Hei på deg!";
        String encryptedMessage = keyPairGenerator.encrypt(message);

        System.out.println("Encrypted message: " + encryptedMessage);
        System.out.println("Decrypted message: " + keyPairGenerator.decrypt(encryptedMessage));

        keyPairGenerator.printKeys();
//        keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
//        keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
//        System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
//        System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
    }
}
