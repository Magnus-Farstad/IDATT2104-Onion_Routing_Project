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
        System.out.println(responseCode);

        Scanner leserFraKommandovindu = new Scanner(System.in);

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

        /* Setter opp forbindelsen til tjenerprogrammet */
        String[] node1 = list[0].split(",");
        Socket forbindelse = new Socket(node1[2], Integer.parseInt(node1[0]));
        System.out.println("Nå er forbi1250,AQNt1urgAvLf7Nht4ViXrYyqAGwydGaYSCCCnTFAaYzsCOYLu0sSajSn+w/yhpnS+OeCMhZ+1Hzu0yJ71lr2C6U5QN0kZsqCJjrk9YDSPMzP5On73ngykU2mu3V3VZawxYeccpi679yyzfWIIahR6WK0cAd4ErIcDjbVvzzr1/Q=,10.22.2.181ndelsen opprettet.");

        /* �pner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


        /* Leser tekst fra kommandovinduet (brukeren) */
        String enLinje = leserFraKommandovindu.nextLine();
        String encryptedMessage = enLinje;
        while (!enLinje.equals("")) {
            encryptedMessage = aeSencryption.encrypt(encryptedMessage,keys.get(list.length-1));
            for(int i= list.length-1; i > 0; i--){
                encryptedMessage += "," + ports.get(i) + "," + addresses.get(i);
                encryptedMessage = aeSencryption.encrypt(encryptedMessage,keys.get(i-1));
            }

            skriveren.println(encryptedMessage);
            System.out.println("Sender kryptert melding..." + encryptedMessage + "\n");

            String respons = leseren.readLine();
            System.out.println("Får respons..." + respons + "\n");

            for(int i = 0; i < list.length; i++ ){
                respons = aeSencryption.decrypt(respons, keys.get(i));
            }

            System.out.println("Dekrypterer respons og får..." + respons + "\n");

            encryptedMessage = leserFraKommandovindu.nextLine();
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}