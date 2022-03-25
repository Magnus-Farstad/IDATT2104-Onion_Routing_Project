package node;

import cryptography.AESencryption;
import cryptography.EncryptionManager;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static API.APIService.*;

public class NodeMain {

    //private final static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";


    public static void main(String[] args) throws Exception {
        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.println("Skriv inn addressen til serveren");

        String serverAddress = leserFraKommandovindu.nextLine();
        String publickey = apiGETRequest("http://" + serverAddress + ":8080/getPublicKey");

        EncryptionManager encryptionManager = new EncryptionManager(publickey);
        encryptionManager.initFromStrings();

        AESencryption aesEncryption = new AESencryption();
        SecretKey aesKey = aesEncryption.generateAESKey();

        String melding = "Hei dette er meg";

        System.out.println("Melding: " + melding);
        String encrypted = aesEncryption.encrypt(melding, aesKey);
        System.out.println("Encrypted message: " + encrypted);
        System.out.println(aesEncryption.decrypt(encrypted, aesKey));

        System.out.println("Skriv inn portnummer til noden");
        String PORTNR = leserFraKommandovindu.nextLine();


        String nodeAddress = InetAddress.getLocalHost().getHostAddress();

        String aesKeyString = aesEncryption.convertSecretKeyToString(aesKey);
        String encryptedAES = encryptionManager.encrypt(aesKeyString);

        int responseCode = apiPOSTNode("http://" + serverAddress + ":8080/postNode", PORTNR, encryptedAES, nodeAddress);
        System.out.println(responseCode);

        ServerSocket tjener = new ServerSocket(Integer.parseInt(PORTNR));
        System.out.println("Logg for node 1. Nå venter vi...");
        Socket forbindelse = tjener.accept();

        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Sender innledning til klienten */
        //skriveren.println("Hei, du har kontakt med node 1!");

        String encryptedMessage = leseren.readLine();
        System.out.println(encryptedMessage);

        System.out.println("SKAL DEKRYPTERE");
        String decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);
        System.out.println(decryptedData);

        if (decryptedData.contains(",")) {

            String[] nextNode = decryptedData.split(",");

            Socket forbindelse2 = new Socket(nextNode[2], Integer.parseInt(nextNode[1]));
            InputStreamReader leseforbindelse2 = new InputStreamReader(forbindelse2.getInputStream());
            BufferedReader leseren2 = new BufferedReader(leseforbindelse2);
            PrintWriter skriveren2 = new PrintWriter(forbindelse2.getOutputStream(), true);

            skriveren2.println(nextNode[0]);

            String messageFromNext = leseren2.readLine();

            System.out.println(messageFromNext);
            String encryptedMesageBack = aesEncryption.encrypt(messageFromNext, aesKey);

            skriveren.println(encryptedMesageBack);
            System.out.println(encryptedMesageBack);

        } else {
            int responseCode2 = apiPOSTString("http://" + serverAddress + ":8080/postMessage", decryptedData);
            System.out.println(responseCode2);
            String message = apiGETRequest("http://" + serverAddress + ":8080/getMessage");
            String encryptedMesageBack = aesEncryption.encrypt(message, aesKey);
            System.out.println(encryptedMesageBack);
            skriveren.println(encryptedMesageBack);
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
