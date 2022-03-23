package node;

import cryptography.AESencryption;
import cryptography.EncryptionManager;
import model.Node;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static API.APIService.apiPOSTNode;

public class Node1 {

    public static void main(String[] args) throws Exception {
        final int PORTNR = 1250;

        EncryptionManager encryptionManager = new EncryptionManager("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs6hMk1chReRa76VAVx42YDGAv+bGwLlxAZybLwA9pFuU2oj1T9pdDWQen4m/Yo0oOFQAKsd1FWnILPtdIU3Is++k4DYMCSEQK+FA4a1wZ5ghx1QiUcu/e08gl9nDtYyGZixZpGb8hM5PToyNTL1AqKDEPOvEEX+Lia9Hp9/7hVAgMBAAECgYAEmiSLrpj2P6FDQcqx6qF6Sccxu9ZNEb7lzE3tjy4eD4kh40h0lZyP53pGzIcbMjSjj5TJP4G+GJpFSpEybEnpjxPIljg42pEJNvpKLxbT86ZCM0WN4qfXL5dA5sWqY+laB59Td+fATnc1Nj0n8CL3/i6PpQwg01M5qoJTPgVZAQJBANmfWXcX88s+XuFB7hFjLGi0euCvMW/dXeETWk1CYQEkMGEG5+ElrGiLFiraO6CIdZ1nOQueD7RQtM5846taKMECQQC2ml+qFhCGzez8HK1cMXEgMNzM3aLhvUmp6fNNhqDSNSYzGy1K6NiJX1CE9yOa525M4nZ1Jh1vPnrws3BOBgCVAkEAmqCUywAj45fPhrJ327bhyQvj+12//MIHgHNlyFuP3WW/UlG71MgV9rpM5+nkUC5lk4/SgqSud+qYbddjVU9cgQJAEbh5gDAT+oERdoXx7Ph/Wfhj9R2tKOsNsweZLPTbtoqh4mPIyXQ/T1WIot64/ddnxN5VUJkaUilmFOXVCD1c4QJBAIaZyp6Q8HIyL7iwClaCAfu8mSt+aB+9s4avPzexFJrvfogunGaU62F6oV78LRsiDbe+6sjG0nGqnWDvkrSDpH0=");
        encryptionManager.initFromStrings();
        AESencryption aesEncryption = new AESencryption();

        SecretKey aesKey = aesEncryption.generateAESKey();
        Node node1 = new Node(aesKey, PORTNR);

        String aesKeyString = aesEncryption.convertSecretKeyToString(aesKey);
        System.out.println(aesKeyString);
        System.out.println("SECRETKEY: " + aesKey);

        int responseCode = apiPOSTNode("http://localhost:8080/postNode", String.valueOf(PORTNR), aesKeyString);
        System.out.println(responseCode);

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for node 1. Nå venter vi...");
        Socket forbindelse = tjener.accept();

        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Sender innledning til klienten */
        skriveren.println("Hei, du har kontakt med node 1!");

        String encryptedMessage = leseren.readLine();

        System.out.println("SKAL DEKRYPTERE");
        String decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);

        System.out.println(decryptedData);


        /*
        Socket forbindelse2 = new Socket("localhost", Integer.parseInt(decryptedData));
        InputStreamReader leseforbindelse2 = new InputStreamReader(forbindelse2.getInputStream());
        BufferedReader leseren2 = new BufferedReader(leseforbindelse2);
        PrintWriter skriveren2 = new PrintWriter(forbindelse2.getOutputStream(), true);


         */
        while (encryptedMessage != null) {
            System.out.println("sender krypter melding og addresse videre til node 2");
            //skriveren2.println(encryptedMessage);
            //String respons = leseren2.readLine();  // mottar respons fra tjeneren
            skriveren.println(decryptedData);
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
