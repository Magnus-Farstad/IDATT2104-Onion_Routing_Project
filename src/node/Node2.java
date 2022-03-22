package node;

import cryptography.EncryptionManager;

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

public class Node2 {

    public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        final int PORTNR = 1251;

        EncryptionManager encryptionManager = new EncryptionManager("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIGb/lY99ffOS5BwEAv8MBb3uHY44tKhos94WSS4TiFJtLzVswsQ0DL406F3zlAapsNALgXvBLdTnDWdWedfpuMpqKKerVuynnpgQvzVZk1yAjo9K8cdfGbwag9A9MrlJXNtXSnisPF/pxZX62bq8eRigCs006FoRXXY4cxAmO8RAgMBAAECgYAAuBn0FOm/vvmmisoXpAXad9yuKFcClC6ncjrGk5Cs3pvTk1BWpTKxFbYuLES7st6FpV4+6i7IJxRuFNFty5cqLzcjA6nW7P6UmKekov+cgnyJl4znhSRMAVfPpQwfgKgB1QYw5oOxYeEY4wI+S2lo5vQnGEZ6U6c8pprjpc+iAQJBAIcZWhEE4kSPntU59Ckl7hjTjYvMsp0K4Bi11hanxpVuRiQ/NsuBk1RD3kPcEVlQnC53uw9Q9uBSUtxZanhUSCUCQQD1mPpLQ9XmFe4AXuia7MAs7NPMir4RuWxYFtse8b/sOH8/X2kfNx7ay0F5ytUr8zyu8DY+xXE5BmDr9eZoxlF9AkA4RLben8oeBDODW7/143ZnoLUzpO4/umfb4uBoTzjGxEcykaGg4TcbwaixWtde+9QRBo1Cs9YfWCpq3FMcCv6BAkBJUL2HMlzsLqe53Js7hGlp/9jKOrC6wcuiEFChUDGm5sa1uFm9Q8smLX8CSJaSZC49WuAWpQJDr6/HQMTjijBRAkBPgAgxln9QtBbqybFsWda74wlJ4kggyWA2LDLsnB9QcfdidZHRnek4IpSISmMU2XWtcGVQlDKCfUP+P+IiCYAB");
        encryptionManager.initFromStrings();

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for node 2. Nå venter vi...");
        Socket forbindelse = tjener.accept();

        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Sender innledning til klienten */

        String encryptedMessage = leseren.readLine();
        String[] nextPorts = encryptedMessage.split(" ");
        String decryptedMessage = encryptionManager.decrypt(nextPorts[2]);
        System.out.println("Kryptert melding fra node2: " + encryptedMessage + "Portnummer til neste node: " + decryptedMessage);

        Socket forbindelse2 = new Socket("localhost", Integer.parseInt(decryptedMessage));
        InputStreamReader leseforbindelse2 = new InputStreamReader(forbindelse2.getInputStream());
        BufferedReader leseren2 = new BufferedReader(leseforbindelse2);
        PrintWriter skriveren2 = new PrintWriter(forbindelse2.getOutputStream(), true);

        while (encryptedMessage != null) {
            System.out.println("Sender kryptert melding videre til node 3");
            skriveren2.println(encryptedMessage);
            String respons = leseren2.readLine();  // mottar respons fra tjeneren
            skriveren.println(respons);
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}

