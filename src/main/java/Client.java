import cryptography.AESEncryption;
import cryptography.RSAKeyPairGenerator;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static API.APIService.*;

/**
 * A class that communicates with several nodes
 *
 * 1. Sends its public key to a REST server
 * 2. Waiting for nodes to connect to REST server and then retrieves the active nodes.
 * 3. Encrypts a message in x nodes layers before sending it
 * 4. Waits for response from endNode and decrypts the message in x layers
 */
public class Client {

    public static void main(String[] args) throws Exception {

        ArrayList<SecretKey> keys = new ArrayList<>();
        ArrayList<String> ports = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();

        //Generating public and private RSA key pair
        final RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        keyPairGenerator.initKeys();
        keyPairGenerator.initFromStrings(keyPairGenerator.getPublicKey(), keyPairGenerator.getPrivateKey());
        AESEncryption aeSencryption = new AESEncryption();

        //Sends public key to REST server
        int responseCode = apiPOSTString("http://localhost:8080/postPublicKey", keyPairGenerator.getPublicKey());
        if(responseCode == 201){
            System.out.println("Successfully posted publickey to server");
        }

        Scanner scanner = new Scanner(System.in);

        //Retrieves node data from REST server
        System.out.println("Waiting for nodes to connect...");
        TimeUnit.SECONDS.sleep(20);
        //Retrieves data from active nodes
        String response = apiGETRequest("http://localhost:8080/getNodes");

        System.out.println(response);


        String[] list = response.split(";");

        for(int i = 0; i < list.length; i++){
            String[] node =  list[i].split(",");
            ports.add(node[0]);
            String decryptedAESKeyString = keyPairGenerator.decrypt(node[1]);
            SecretKey aesKey = aeSencryption.convertStringToSecretKeyto(decryptedAESKeyString);
            keys.add(aesKey);
            addresses.add(node[2]);
        }

        // Her kan man vel bruke de tabellene man har fylt inn i over?
        String[] node1 = list[0].split(",");
        Socket connection = new Socket(node1[2], Integer.parseInt(node1[0]));

        //Starting communication to nodes
        InputStreamReader readerConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readerConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        System.out.println("You can now write a message...");
        String text = scanner.nextLine();
        String encryptedMessage = text;

        //encrypts message in layers. As many layers as it is nodes
        while (!text.equals("")) {
            encryptedMessage = aeSencryption.encrypt(encryptedMessage, keys.get(list.length-1));
            for(int i= list.length-1; i > 0; i--){
                encryptedMessage += "," + ports.get(i) + "," + addresses.get(i);
                encryptedMessage = aeSencryption.encrypt(encryptedMessage,keys.get(i-1));
            }

            //Sends encrypted message to first node in path
            writer.println(encryptedMessage);
            System.out.println("Sending encrypted message..." + encryptedMessage + "\n");

            //Receives encrypted response from nodes
            String respons = reader.readLine();
            System.out.println("Received response..." + respons + "\n");

            //Decrypts all layers in encrypted message
            for(int i = 0; i < list.length; i++ ){
                respons = aeSencryption.decrypt(respons, keys.get(i));
            }

            System.out.println("Decrypting response..." + respons + "\n");

            encryptedMessage = scanner.nextLine();
        }

        reader.close();
        writer.close();
        connection.close();
    }
}