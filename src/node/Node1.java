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
import java.util.Scanner;

public class Node1 {

    public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        final int PORTNR = 1250;
        final int PORTNR1 = 1251;

        RSAUtil1 rsaUtil1 = new RSAUtil1();
        rsaUtil1.initFromStrings();

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for node 1. Nå venter vi...");
        Socket connectionWithClient = tjener.accept();

        // Lesing og skriving til klienten
        InputStreamReader leseforbindelse = new InputStreamReader(connectionWithClient.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(connectionWithClient.getOutputStream(), true);

        /* Sender innledning til klienten */
        skriveren.println("Hei, du har kontakt med node 1!");

        String nextNodeEncrypted = leseren.readLine();
        System.out.println(nextNodeEncrypted);
        String nextNode = rsaUtil1.decrypt(nextNodeEncrypted);
        skriveren.println("Takk for melding!");

        Socket connectionNextNode = new Socket(nextNode, PORTNR1);
        System.out.println(nextNode);

        // Lesing og skriving til neste node
        InputStreamReader nesteNodeForbindelse = new InputStreamReader(connectionNextNode.getInputStream());
        BufferedReader reader = new BufferedReader(nesteNodeForbindelse);
        PrintWriter writer = new PrintWriter(connectionNextNode.getOutputStream(), true);

        System.out.println(reader.readLine());
        writer.println("Hei dette er node 1");



        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        connectionWithClient.close();
    }
}
