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

public class Node3 {

    public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        final int PORTNR = 1252;

        EncryptionManager encryptionManager = new EncryptionManager("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANRn+zP44nd22OQ11voU/FsXtUq4MeAInLk8IRp42KqQKqHdXAIkFFnn1UeLiTTF28xWprxaKgnN3u1qM0ZGBomK6vwX/xxPfEu959WbEqIjIPhlSVZoD/1tmugYvR1QT3N7dHnOBzNpT1IwkdZ5gkn/DtyQFznF4gCwu/eyVusvAgMBAAECgYAK7CUQtMHpGS+pVSYA87t0Xbg8guWT9lH7sxVc/J+7pQwR5KEWGatKhtQL7UwLJVHRZKniuzfveUXk619+n0DJ/250WMG6Hv0VzuaHgFNCjPi7PWBuySVORGYqZuWAG9VsZCDmGEdgBAAtzay2MqwGx5PwJ6fmqG2Ko/HP1SL4JQJBAN5mhZrTo+oDkcH2aE8e2R909mMRczmBSTZhiBRgZOgwdIWcoIUQKvVrvCEMwz5ZNCCwkr6/wgYdFlh6hwpBel0CQQD0fuxup8KXF5is8XpRrxS0Wy8E7dPhWF1Y+nimwZ7GxhB+MMuJ3dnHDxQydMTiYuhag1A5Wigp+MKfaMSKKJr7AkAPqvdUnf5ZOSEmof5dPJYdQjctaYhNj88hlqNolBXnyaob05n3Zdkw6wMY7PZASTaD6wybhZTcq2Xsm80xqsU9AkEAx9FHd7Qhg4xkWctM1Z9KQ5BWICgixwOJ3uNtYZPSKM/MwOUuI7Gtf1MihY4LLp35GahCE21Mb+j/XnqoTeWbqwJAPPPFvxIZzbkJ08biNt0+Q1gqpsC9vFU75PQfSp0mXqTrd13TfVUQMRph7pXcQO3oXqAiAolw77H3/0Fjm1ioLw==");
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
        String decryptedMessage = encryptionManager.decrypt(nextPorts[3]);

        System.out.println("Kryptert melding fra node3: " + encryptedMessage + "Portnummer til neste node: " + decryptedMessage);

        Socket forbindelse2 = new Socket("localhost", Integer.parseInt(decryptedMessage));
        InputStreamReader leseforbindelse2 = new InputStreamReader(forbindelse2.getInputStream());
        BufferedReader leseren2 = new BufferedReader(leseforbindelse2);
        PrintWriter skriveren2 = new PrintWriter(forbindelse2.getOutputStream(), true);

        while (encryptedMessage != null) {
            System.out.println("Sender kryptert melding videre til end node");
            skriveren2.println(nextPorts[0]);
            String respons = leseren2.readLine();  // mottar respons fra tjeneren
            skriveren.println(respons);
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
