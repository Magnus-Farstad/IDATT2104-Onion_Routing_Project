import cryptography.RSAKeyPairGenerator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SocketThread extends Thread {

    public Socket forbindelse;

    public SocketThread(Socket forbindelse){
        this.forbindelse = forbindelse;
    }

    @Override
    public void run(){
        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        keyPairGenerator.initFromStrings();
        try {
            /* åpner strømmer for kommunikasjon med klientprogrammet */
            InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
            BufferedReader leseren = new BufferedReader(leseforbindelse);
            PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


            /* Sender innledning til klienten */
            skriveren.println("Hei, du har kontakt med tjenersiden!");
            System.out.println(leseren.readLine());

            String encryptedMessage = leseren.readLine();
            while (encryptedMessage != null) {
                String decryptedMessage = keyPairGenerator.decrypt(encryptedMessage);
                System.out.println("En klient skrev: " + encryptedMessage);
                System.out.println("Dekryptert: " + decryptedMessage);
                skriveren.println("Tjener svarer: " + decryptedMessage);
                encryptedMessage = leseren.readLine();
            }


            /* Lukker forbindelsen */
            leseren.close();
            skriveren.close();
            forbindelse.close();

        } catch (IOException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException exception) {
            exception.printStackTrace();
        }
    }
}



