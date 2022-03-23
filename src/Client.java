import cryptography.AESencryption;
import cryptography.RSAKeyPairGenerator;
import model.Node;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import static API.APIService.apiGETRequest;

public class Client {

    private static final String PRIVATE_KEY_1 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs6hMk1chReRa76VAVx42YDGAv+bGwLlxAZybLwA9pFuU2oj1T9pdDWQen4m/Yo0oOFQAKsd1FWnILPtdIU3Is++k4DYMCSEQK+FA4a1wZ5ghx1QiUcu/e08gl9nDtYyGZixZpGb8hM5PToyNTL1AqKDEPOvEEX+Lia9Hp9/7hVAgMBAAECgYAEmiSLrpj2P6FDQcqx6qF6Sccxu9ZNEb7lzE3tjy4eD4kh40h0lZyP53pGzIcbMjSjj5TJP4G+GJpFSpEybEnpjxPIljg42pEJNvpKLxbT86ZCM0WN4qfXL5dA5sWqY+laB59Td+fATnc1Nj0n8CL3/i6PpQwg01M5qoJTPgVZAQJBANmfWXcX88s+XuFB7hFjLGi0euCvMW/dXeETWk1CYQEkMGEG5+ElrGiLFiraO6CIdZ1nOQueD7RQtM5846taKMECQQC2ml+qFhCGzez8HK1cMXEgMNzM3aLhvUmp6fNNhqDSNSYzGy1K6NiJX1CE9yOa525M4nZ1Jh1vPnrws3BOBgCVAkEAmqCUywAj45fPhrJ327bhyQvj+12//MIHgHNlyFuP3WW/UlG71MgV9rpM5+nkUC5lk4/SgqSud+qYbddjVU9cgQJAEbh5gDAT+oERdoXx7Ph/Wfhj9R2tKOsNsweZLPTbtoqh4mPIyXQ/T1WIot64/ddnxN5VUJkaUilmFOXVCD1c4QJBAIaZyp6Q8HIyL7iwClaCAfu8mSt+aB+9s4avPzexFJrvfogunGaU62F6oV78LRsiDbe+6sjG0nGqnWDvkrSDpH0=";
    private static final String PUBLIC_KEY_1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";


    public static void main(String[] args) throws Exception {
        final int PORTNR1 = 1251;
        final int PORTNR2 = 1252;
        final int PORTNR3 = 1253;

        final RSAKeyPairGenerator keyPairGenerator1 = new RSAKeyPairGenerator();
        keyPairGenerator1.initFromStrings(PUBLIC_KEY_1, PRIVATE_KEY_1);



        /* Bruker en scanner til å lese fra kommandovinduet */
        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.print("Oppgi navnet på maskinen der tjenerprogrammet kjører: ");
        String tjenermaskin = leserFraKommandovindu.nextLine();

        /* Setter opp forbindelsen til tjenerprogrammet */
        Socket forbindelse = new Socket(tjenermaskin, 1250);
        System.out.println("Nå er forbindelsen opprettet.");

        /* �pner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        String response = apiGETRequest("http://localhost:8080/getNodes");
        System.out.println(response);
        String[] list = response.split(",");
        String aesKeyString = list[1];
        String decryptedAESKeyString = keyPairGenerator1.decrypt(aesKeyString);

        AESencryption aeSencryption = new AESencryption();
        SecretKey aesKey = aeSencryption.convertStringToSecretKeyto(decryptedAESKeyString);
        System.out.println("SECRETKEY: " + aesKey);



        /* Leser innledning fra tjeneren og skriver den til kommandovinduet */
        String innledning1 = leseren.readLine();
        System.out.println(innledning1);

        /* Leser tekst fra kommandovinduet (brukeren) */
        String enLinje = leserFraKommandovindu.nextLine();
        while (!enLinje.equals("")) {
            /*
            String encryptedMessage = keyPairGenerator1.encrypt(enLinje);
            encryptedMessage += PORTNR3;
            encryptedMessage = keyPairGenerator3.encrypt(encryptedMessage);
            encryptedMessage += PORTNR2;
            encryptedMessage = keyPairGenerator2.encrypt(encryptedMessage);
            encryptedMessage += PORTNR1;
            encryptedMessage = keyPairGenerator1.encrypt(encryptedMessage);

             */

            String encryptedMessage = aeSencryption.encrypt(enLinje, aesKey);
            skriveren.println(encryptedMessage);
            String respons = leseren.readLine();  // mottar respons fra tjeneren
            System.out.println(respons);
            enLinje = leserFraKommandovindu.nextLine();
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}