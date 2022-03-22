import cryptography.keygen.AES;
import cryptography.keygen.RSAKeyPairGenerator;
import model.Payload;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Client {

    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";

    public static void main(String[] args) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        final int PORTNR = 1250;
        final RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        final AES aes = new AES();

        keyPairGenerator.initFromStrings();
        aes.initFromStrings("o42ao5pv1TEoTsQ3rN1ASg==", "jpckxf77xxMcS51A");

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
        String message = "Hei dette er melding fra objekt nummer 1";

        String encryptedMessage = keyPairGenerator.encrypt(message);

        Payload payload = new Payload(encryptedMessage);

        /* Neste payload blir satt */
        String secondMessage = "Hei dette er melding fra objekt nummer 2";
        String encryptedSecondMessage = keyPairGenerator.encrypt(secondMessage);

        Payload payload2 = new Payload();
        payload2.setData(secondMessage);

        byte[] serializedPayload = keyPairGenerator.serializeObject(payload2);
        byte[] encryptedBytes = aes.encryptBytes(serializedPayload);

        payload2.setData(encryptedSecondMessage);
        payload.setNextPayload(encryptedBytes);

        byte[] decryptedBytes = aes.decryptBytes(encryptedBytes);
        Payload deserializedPayload = keyPairGenerator.deserializeObject(decryptedBytes);
        System.out.println("Dekryptert objekt" + deserializedPayload.getData());

        /* Leser innledning fra tjeneren og skriver den til kommandovinduet */
        String innledning1 = leseren.readLine();
        System.out.println(innledning1);

        System.out.println("Sending message to server");
        objectOutputStream.writeObject(payload);




        /* Leser tekst fra kommandovinduet (brukeren) */
        String enLinje = leserFraKommandovindu.nextLine();
        String encryptedMessageString = keyPairGenerator.encrypt(enLinje);
        skriveren.println(encryptedMessageString);
        System.out.println(leseren.readLine());
        System.out.println(leseren.readLine());


        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        outputStream.close();
        objectOutputStream.close();
        socket.close();

    }
}