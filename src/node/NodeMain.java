package node;

import cryptography.AESencryption;
import cryptography.EncryptionManager;
import model.Node;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static API.APIService.apiGETRequest;
import static API.APIService.apiPOSTNode;

public class NodeMain {


    public static void main(String[] args) throws Exception {
        String publickey = apiGETRequest("http://localhost:8080/getPublicKey");

        EncryptionManager encryptionManager = new EncryptionManager(publickey);
        encryptionManager.initFromStrings();

        AESencryption aesEncryption = new AESencryption();
        SecretKey aesKey = aesEncryption.generateAESKey();

        System.out.println("Skriv inn portnummer til noden");
        Scanner leserFraKommandovindu = new Scanner(System.in);
        String PORTNR = leserFraKommandovindu.nextLine();

        System.out.println("Skriv inn addressen til serveren");
        String serverAddress = leserFraKommandovindu.nextLine();

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
