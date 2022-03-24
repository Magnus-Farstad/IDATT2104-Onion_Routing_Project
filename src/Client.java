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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static API.APIService.*;

public class Client {

    private static final String PRIVATE_KEY_1 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs6hMk1chReRa76VAVx42YDGAv+bGwLlxAZybLwA9pFuU2oj1T9pdDWQen4m/Yo0oOFQAKsd1FWnILPtdIU3Is++k4DYMCSEQK+FA4a1wZ5ghx1QiUcu/e08gl9nDtYyGZixZpGb8hM5PToyNTL1AqKDEPOvEEX+Lia9Hp9/7hVAgMBAAECgYAEmiSLrpj2P6FDQcqx6qF6Sccxu9ZNEb7lzE3tjy4eD4kh40h0lZyP53pGzIcbMjSjj5TJP4G+GJpFSpEybEnpjxPIljg42pEJNvpKLxbT86ZCM0WN4qfXL5dA5sWqY+laB59Td+fATnc1Nj0n8CL3/i6PpQwg01M5qoJTPgVZAQJBANmfWXcX88s+XuFB7hFjLGi0euCvMW/dXeETWk1CYQEkMGEG5+ElrGiLFiraO6CIdZ1nOQueD7RQtM5846taKMECQQC2ml+qFhCGzez8HK1cMXEgMNzM3aLhvUmp6fNNhqDSNSYzGy1K6NiJX1CE9yOa525M4nZ1Jh1vPnrws3BOBgCVAkEAmqCUywAj45fPhrJ327bhyQvj+12//MIHgHNlyFuP3WW/UlG71MgV9rpM5+nkUC5lk4/SgqSud+qYbddjVU9cgQJAEbh5gDAT+oERdoXx7Ph/Wfhj9R2tKOsNsweZLPTbtoqh4mPIyXQ/T1WIot64/ddnxN5VUJkaUilmFOXVCD1c4QJBAIaZyp6Q8HIyL7iwClaCAfu8mSt+aB+9s4avPzexFJrvfogunGaU62F6oV78LRsiDbe+6sjG0nGqnWDvkrSDpH0=";
    private static final String PUBLIC_KEY_1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";


    public static void main(String[] args) throws Exception {

        ArrayList<SecretKey> keys = new ArrayList<>();
        ArrayList<String> ports = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();



        final RSAKeyPairGenerator keyPairGenerator1 = new RSAKeyPairGenerator();
        keyPairGenerator1.initFromStrings(PUBLIC_KEY_1, PRIVATE_KEY_1);
        AESencryption aeSencryption = new AESencryption();

        int responseCode = apiPOSTKey("http://localhost:8080/postPublicKey", PUBLIC_KEY_1);
        System.out.println(responseCode);

        Scanner leserFraKommandovindu = new Scanner(System.in);

        TimeUnit.SECONDS.sleep(20);
        String response = apiGETRequest("http://localhost:8080/getNodes");

        System.out.println(response);

        String[] list = response.split(";");

        for(int i = 0; i < list.length; i++){
            String[] node =  list[i].split(",");
            ports.add(node[0]);
            String decryptedAESKeyString = keyPairGenerator1.decrypt(node[1]);
            SecretKey aesKey = aeSencryption.convertStringToSecretKeyto(decryptedAESKeyString);
            keys.add(aesKey);
            addresses.add(node[2]);
        }

        /* Setter opp forbindelsen til tjenerprogrammet */
        String[] node1 = list[0].split(",");
        Socket forbindelse = new Socket(node1[2], Integer.parseInt(node1[0]));
        System.out.println("Nå er forbi1250,AQNt1urgAvLf7Nht4ViXrYyqAGwydGaYSCCCnTFAaYzsCOYLu0sSajSn+w/yhpnS+OeCMhZ+1Hzu0yJ71lr2C6U5QN0kZsqCJjrk9YDSPMzP5On73ngykU2mu3V3VZawxYeccpi679yyzfWIIahR6WK0cAd4ErIcDjbVvzzr1/Q=,10.22.2.181ndelsen opprettet.");

        /* �pner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


        /* Leser tekst fra kommandovinduet (brukeren) */
        String enLinje = leserFraKommandovindu.nextLine();
        String encryptedMessage = enLinje;
        while (!enLinje.equals("")) {
            encryptedMessage = aeSencryption.encrypt(encryptedMessage,keys.get(list.length-1));
            for(int i= list.length-1; i > 0; i--){
                encryptedMessage += "," + ports.get(i) + "," + addresses.get(i);
                encryptedMessage = aeSencryption.encrypt(encryptedMessage,keys.get(i-1));
            }
            skriveren.println(encryptedMessage);
            String respons = leseren.readLine();
            System.out.println(respons);

            for(int i = 0; i < list.length; i++ ){
                respons = aeSencryption.decrypt(respons, keys.get(i));
            }

            System.out.println(respons);

            enLinje = leserFraKommandovindu.nextLine();
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}