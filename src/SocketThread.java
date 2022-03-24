import cryptography.EncryptionManager;
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

        String fewrg = " W4G";
        EncryptionManager encryptionManager = new EncryptionManager(fewrg);
        encryptionManager.initFromStrings();
        try {
            /* åpner strømmer for kommunikasjon med klientprogrammet */
            InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
            BufferedReader leseren = new BufferedReader(leseforbindelse);
            PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


            String encryptedMessage = leseren.readLine();

            String decryptedMessage = encryptionManager.encrypt(encryptedMessage);
            System.out.println("Klient skrev kryptert melding: " + encryptedMessage);
            System.out.println("Dekryptert: " + decryptedMessage);
            skriveren.println("Endnode svarer: " + decryptedMessage);


            /* Lukker forbindelsen */
            leseren.close();
            skriveren.close();
            forbindelse.close();

        } catch (IOException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException exception) {
            exception.printStackTrace();
        }
    }
}



