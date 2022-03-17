package node;

import cryptography.RSAUtil1;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Node1 {

    public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        final int PORTNR = 1250;

        RSAUtil1 rsaUtil1 = new RSAUtil1();
        rsaUtil1.initFromStrings();

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for node 1. Nå venter vi...");
        Socket forbindelse = tjener.accept();

        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


        /* Sender innledning til klienten */
        skriveren.println("Hei, du har kontakt med node 1!");

        String encryptedMessage = leseren.readLine();
        while (encryptedMessage != null) {
            String decryptedMessage = rsaUtil1.decrypt(encryptedMessage);
            System.out.println("En klient skrev: " + encryptedMessage);
            System.out.println("Dekryptert: " + decryptedMessage);
            skriveren.println("Tjener svarer: " + decryptedMessage);
            encryptedMessage = leseren.readLine();
        }


        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
