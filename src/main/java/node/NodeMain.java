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

        System.out.println("Skriv inn portnummer til noden");
        String PORTNR = leserFraKommandovindu.nextLine();

        String nodeAddress = InetAddress.getLocalHost().getHostAddress();

        String aesKeyString = aesEncryption.convertSecretKeyToString(aesKey);
        String encryptedAES = encryptionManager.encrypt(aesKeyString);

        int responseCode = apiPOSTNode("http://" + serverAddress + ":8080/postNode", PORTNR, encryptedAES, nodeAddress);
        if(responseCode == 201){
            System.out.println("The node was successfully stored\n");
        }

        ServerSocket tjener = new ServerSocket(Integer.parseInt(PORTNR));
        System.out.println("Logg for node. Nå venter vi...");
        System.out.println("-------------------------------\n");
        Socket forbindelse = tjener.accept();

        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


        String encryptedMessage = leseren.readLine();
        System.out.println("Leser inn melding..." + encryptedMessage + "\n" );

        String decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);
        System.out.println("Dekrypterer melding..." + decryptedData + "\n");


        if (decryptedData.contains(",")) {

            String[] nextNode = decryptedData.split(",");

            Socket forbindelse2 = new Socket(nextNode[2], Integer.parseInt(nextNode[1]));
            InputStreamReader leseforbindelse2 = new InputStreamReader(forbindelse2.getInputStream());
            BufferedReader leseren2 = new BufferedReader(leseforbindelse2);
            PrintWriter skriveren2 = new PrintWriter(forbindelse2.getOutputStream(), true);

            String message = nextNode[0];
            while(!message.equals("")){
                skriveren2.println(message);
                System.out.println("Sender melding videre... " + message + "\n");

                String messageFromNext = leseren2.readLine();
                System.out.println("Får tilbake svar... " + messageFromNext + "\n");

                String encryptedMesageBack = aesEncryption.encrypt(messageFromNext, aesKey);

                skriveren.println(encryptedMesageBack);
                System.out.println("Krypterer og sender videre..." + encryptedMesageBack + "\n\n");

                encryptedMessage = leseren.readLine();
                System.out.println("Logg for node. Nå venter vi...");
                System.out.println("--------------------------------\n");

                System.out.println("Leser inn melding..." + encryptedMessage + "\n" );

                decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);
                System.out.println("Dekrypterer melding..." + decryptedData + "\n");

                nextNode = decryptedData.split(",");
                message = nextNode[0];
            }

        } else {

            while(!decryptedData.equals("")){
                System.out.println("Sender melding til server..." + decryptedData + "\n");
                apiPOSTString("http://" + serverAddress + ":8080/postMessage", decryptedData);

                String message = apiGETRequest("http://" + serverAddress + ":8080/getMessage");
                System.out.println("Henter respons fra serveren... " + message + "\n");

                String encryptedMesageBack = aesEncryption.encrypt(message, aesKey);
                System.out.println("Krypter meldingen, og sender tilbake... " + encryptedMesageBack + "\n\n");
                skriveren.println(encryptedMesageBack);


                encryptedMessage = leseren.readLine();
                System.out.println("Logg for node. Nå venter vi...");
                System.out.println("--------------------------------\n");

                System.out.println("Leser inn melding..." + encryptedMessage + "\n");

                decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);
                System.out.println("Dekrypterer melding..." + decryptedData + "\n");
            }
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
