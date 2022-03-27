package node;

import cryptography.AESEncryption;
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

/**
 * A class that represents a node
 * Retrieves public key from client to encrypt EAS keys
 * Post its data to REST server
 * Waiting for message from client and decrypts it
 * finds next node if the message still contains (,)
 * If not encrypts message and sends back to another node
 */

public class NodeMain {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please write the address to the server: ");

        String serverAddress = scanner.nextLine();
        String publickey = apiGETRequest("http://" + serverAddress + ":8080/getPublicKey");

        EncryptionManager encryptionManager = new EncryptionManager(publickey);
        encryptionManager.initFromStrings();

        AESEncryption aesEncryption = new AESEncryption();
        SecretKey aesKey = aesEncryption.generateAESKey();

        System.out.println("Please write the portnumber to this node: ");
        String PORTNR = scanner.nextLine();

        String nodeAddress = InetAddress.getLocalHost().getHostAddress();

        String aesKeyString = aesEncryption.convertSecretKeyToString(aesKey);
        String encryptedAES = encryptionManager.encrypt(aesKeyString);

        int responseCode = apiPOSTNode("http://" + serverAddress + ":8080/postNode", PORTNR, encryptedAES, nodeAddress);
        if(responseCode == 201){
            System.out.println("The node was successfully stored\n");
        }

        ServerSocket client = new ServerSocket(Integer.parseInt(PORTNR));
        System.out.println("A Log for this node. Waiting...");
        System.out.println("-------------------------------\n");
        Socket connection = client.accept();

        InputStreamReader readerConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readerConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        String encryptedMessage = reader.readLine();
        System.out.println("Reading message..." + encryptedMessage + "\n" );

        String decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);
        System.out.println("Decrypting message..." + decryptedData + "\n");


        if (decryptedData.contains(",")) {

            String[] nextNode = decryptedData.split(",");

            Socket connectionNext = new Socket(nextNode[2], Integer.parseInt(nextNode[1]));
            InputStreamReader readerConnectionNext = new InputStreamReader(connectionNext.getInputStream());
            BufferedReader readerNext = new BufferedReader(readerConnectionNext);
            PrintWriter writerNext = new PrintWriter(connectionNext.getOutputStream(), true);

            String message = nextNode[0];
            while(!message.equals("")){
                writerNext.println(message);
                System.out.println("Sending message to next node... " + message + "\n");

                String messageFromNext = readerNext.readLine();
                System.out.println("Received respons... " + messageFromNext + "\n");

                String encryptedMesageBack = aesEncryption.encrypt(messageFromNext, aesKey);

                writer.println(encryptedMesageBack);
                System.out.println("Encrypting and sending back..." + encryptedMesageBack + "\n\n");

                encryptedMessage = reader.readLine();
                System.out.println("A Log for this node. Waiting...");
                System.out.println("-------------------------------\n");

                System.out.println("Reading message..." + encryptedMessage + "\n" );

                decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);
                System.out.println("Decrypting message..." + decryptedData + "\n");

                nextNode = decryptedData.split(",");
                message = nextNode[0];
            }

        } else {

            while(!decryptedData.equals("")){
                System.out.println("Sending message to server..." + decryptedData + "\n");
                apiPOSTString("http://" + serverAddress + ":8080/postMessage", decryptedData);

                String message = apiGETRequest("http://" + serverAddress + ":8080/getMessage");
                System.out.println("Receiving response from server... " + message + "\n");

                String encryptedMesageBack = aesEncryption.encrypt(message, aesKey);
                System.out.println("Encrypting message, and sends it back... " + encryptedMesageBack + "\n\n");
                writer.println(encryptedMesageBack);

                encryptedMessage = reader.readLine();
                System.out.println("A Log for this node. Waiting...");
                System.out.println("-------------------------------\n");

                System.out.println("Reading message..." + encryptedMessage + "\n");

                decryptedData = aesEncryption.decrypt(encryptedMessage, aesKey);
                System.out.println("Decrypting message..." + decryptedData + "\n");
            }
        }

        /* Closing connection */
        reader.close();
        writer.close();
        connection.close();
    }
}
