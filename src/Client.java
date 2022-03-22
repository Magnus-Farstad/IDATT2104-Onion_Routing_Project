import cryptography.RSAKeyPairGenerator;
import model.Payload;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Client {

    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";

    public static void main(String[] args) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        final int PORTNR = 1250;
        final RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        keyPairGenerator.initFromStrings();

        /* Bruker en scanner til å lese fra kommandovinduet */
        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.print("Oppgi navnet på maskinen der tjenerprogrammet kjører: ");
        String tjenermaskin = leserFraKommandovindu.nextLine();

        /* Setter opp forbindelsen til tjenerprogrammet */
        Socket socket = new Socket(tjenermaskin, PORTNR);
        System.out.println("Nå er forbindelsen opprettet.");

        /* �pner en forbindelse for kommunikasjon med tjenerprogrammet via String */
        InputStreamReader leseforbindelse = new InputStreamReader(socket.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(socket.getOutputStream(), true);

        /* Åpner en forbindelse for kommunikasjon med tjenerprogrammet via objekter */
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        Payload payload = new Payload("Dette er fra objektet");

        /* Leser innledning fra tjeneren og skriver den til kommandovinduet */
        String innledning1 = leseren.readLine();
        System.out.println(innledning1);

        System.out.println("Sending message to server");
        objectOutputStream.writeObject(payload);




        /* Leser tekst fra kommandovinduet (brukeren) */
        String enLinje = leserFraKommandovindu.nextLine();
        String encryptedMessage = keyPairGenerator.encrypt(enLinje);
        skriveren.println(encryptedMessage);
        System.out.println(leseren.readLine());
        System.out.println(leseren.readLine());


        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        socket.close();
    }
}