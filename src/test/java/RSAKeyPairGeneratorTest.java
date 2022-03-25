import cryptography.AESencryption;
import cryptography.RSAKeyPairGenerator;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@DisplayName("RSA Key pair generator test")
public class RSAKeyPairGeneratorTest {

    @DisplayName("Successfully generate keys")
    @Test
    public void successfullyGenerateKeys() throws NoSuchAlgorithmException {
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        rsaKeyPairGenerator.initKeys();

        String public_key = rsaKeyPairGenerator.getPublicKey();
        String private_key = rsaKeyPairGenerator.getPrivateKey();

        Assertions.assertNotNull(public_key);
        Assertions.assertNotNull(private_key);

    }

    @DisplayName("Encrypt AES key with RSA public key")
    @Test
    public void encryptAESWithRSA() throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {

        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();

        rsaKeyPairGenerator.initFromStrings("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs6hMk1chReRa76VAVx42YDGAv+bGwLlxAZybLwA9pFuU2oj1T9pdDWQen4m/Yo0oOFQAKsd1FWnILPtdIU3Is++k4DYMCSEQK+FA4a1wZ5ghx1QiUcu/e08gl9nDtYyGZixZpGb8hM5PToyNTL1AqKDEPOvEEX+Lia9Hp9/7hVAgMBAAECgYAEmiSLrpj2P6FDQcqx6qF6Sccxu9ZNEb7lzE3tjy4eD4kh40h0lZyP53pGzIcbMjSjj5TJP4G+GJpFSpEybEnpjxPIljg42pEJNvpKLxbT86ZCM0WN4qfXL5dA5sWqY+laB59Td+fATnc1Nj0n8CL3/i6PpQwg01M5qoJTPgVZAQJBANmfWXcX88s+XuFB7hFjLGi0euCvMW/dXeETWk1CYQEkMGEG5+ElrGiLFiraO6CIdZ1nOQueD7RQtM5846taKMECQQC2ml+qFhCGzez8HK1cMXEgMNzM3aLhvUmp6fNNhqDSNSYzGy1K6NiJX1CE9yOa525M4nZ1Jh1vPnrws3BOBgCVAkEAmqCUywAj45fPhrJ327bhyQvj+12//MIHgHNlyFuP3WW/UlG71MgV9rpM5+nkUC5lk4/SgqSud+qYbddjVU9cgQJAEbh5gDAT+oERdoXx7Ph/Wfhj9R2tKOsNsweZLPTbtoqh4mPIyXQ/T1WIot64/ddnxN5VUJkaUilmFOXVCD1c4QJBAIaZyp6Q8HIyL7iwClaCAfu8mSt+aB+9s4avPzexFJrvfogunGaU62F6oV78LRsiDbe+6sjG0nGqnWDvkrSDpH0=");

        AESencryption aesEncryption = new AESencryption();

        SecretKey aesKey = aesEncryption.generateAESKey();

        String aesKeyString = aesEncryption.convertSecretKeyToString(aesKey);
        String encryptedAES = rsaKeyPairGenerator.encrypt(aesKeyString);

        String decryptedAES = rsaKeyPairGenerator.decrypt(encryptedAES);

        Assertions.assertNotEquals(aesKeyString, encryptedAES);
        Assertions.assertEquals(aesKeyString, decryptedAES);
    }
}
