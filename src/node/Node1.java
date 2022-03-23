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

    private static final String PUBLIC_KEY_1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";

    public static void main(String[] args) throws Exception {
        final int PORTNR = 1250;

        EncryptionManager encryptionManager = new EncryptionManager();
        encryptionManager.initFromStrings();
        AESencryption aesEncryption = new AESencryption();

        SecretKey aesKey = aesEncryption.generateAESKey();
        Node node1 = new Node(aesKey, PORTNR);

        String aesKeyString = aesEncryption.convertSecretKeyToString(aesKey);
        String encryptedAES = encryptionManager.encrypt(aesKeyString);

        int responseCode = apiPOSTNode("http://localhost:8080/postNode", String.valueOf(PORTNR), encryptedAES);
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
