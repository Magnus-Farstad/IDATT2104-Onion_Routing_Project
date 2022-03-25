import cryptography.AESEncryption;
import cryptography.RSAKeyPairGenerator;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static API.APIService.*;

public class Client {

    public static void main(String[] args) throws Exception {

        ArrayList<SecretKey> keys = new ArrayList<>();
        ArrayList<String> ports = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();

        final RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        keyPairGenerator.initKeys();
        keyPairGenerator.initFromStrings(keyPairGenerator.getPublicKey(), keyPairGenerator.getPrivateKey());
        AESEncryption aeSencryption = new AESEncryption();

        int responseCode = apiPOSTString("http://localhost:8080/postPublicKey", keyPairGenerator.getPublicKey());
        if(responseCode == 201){
            System.out.println("Successfully posted publickey to server");
        }

        Scanner scanner = new Scanner(System.in);

        TimeUnit.SECONDS.sleep(20);
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

        String[] node1 = list[0].split(",");
        Socket connection = new Socket(node1[2], Integer.parseInt(node1[0]));

        InputStreamReader readerConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readerConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);


        String text = scanner.nextLine();
        String encryptedMessage = text;
        while (!text.equals("")) {
            encryptedMessage = aeSencryption.encrypt(encryptedMessage,keys.get(list.length-1));
            for(int i= list.length-1; i > 0; i--){
                encryptedMessage += "," + ports.get(i) + "," + addresses.get(i);
                encryptedMessage = aeSencryption.encrypt(encryptedMessage,keys.get(i-1));
            }

            writer.println(encryptedMessage);
            System.out.println("Sending encrypted message..." + encryptedMessage + "\n");

            String respons = reader.readLine();
            System.out.println("Received response..." + respons + "\n");

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