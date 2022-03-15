import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketThread extends Thread{

    public Socket forbindelse;

    public SocketThread(Socket forbindelse){
        this.forbindelse = forbindelse;
    }

    @Override
    public void run(){
        try {
            /* åpner strømmer for kommunikasjon med klientprogrammet */
            InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
            BufferedReader leseren = new BufferedReader(leseforbindelse);
            PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

            /* Sender innledning til klienten */
            skriveren.println("Hei, du har kontakt med tjenersiden!");

            String melding = leseren.readLine();
            while (melding!= null) {
                System.out.println("En klient skrev: " + melding);
                skriveren.println("Tjener svarer: " + melding);
                melding = leseren.readLine();
            }


            /* Lukker forbindelsen */
            leseren.close();
            skriveren.close();
            forbindelse.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}



