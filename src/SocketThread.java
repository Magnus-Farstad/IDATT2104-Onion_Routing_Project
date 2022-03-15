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
            skriveren.println("Skriv inn et regnestykke på formen: (operand operator operand)");

            /* Mottar data fra klienten */
            String regnestykke = leseren.readLine();
            Integer result = null;

            while (regnestykke != null) {  // forbindelsen på klientsiden er lukket
                System.out.println( "Tråd " + this.getId() + " sender regnestykket: " + regnestykke);

                StringTokenizer st = new StringTokenizer(regnestykke);

                while (st.hasMoreTokens()){
                    int oprnd1 = Integer.parseInt(st.nextToken());
                    String operation = st.nextToken();
                    int oprnd2 = Integer.parseInt(st.nextToken());

                    //Tjeneren regner ut regnestykket motatt fra Klient avhengig av operator type
                    if (operation.equals("+")) {
                        result = oprnd1 + oprnd2;
                    }
                    else if (operation.equals("-")) {
                        result = oprnd1 - oprnd2;
                    }
                    else if (operation.equals("*")) {
                        result = oprnd1 * oprnd2;
                    }
                    else if(operation.equals(("/"))) {
                        result = oprnd1 / oprnd2;
                    }
                    else {
                        result = null;
                    }
                    //Skriver tilbake resultat av regnestykket til klient
                    System.out.println("Sender resultatet tilbake til klient...");
                    skriveren.println(result);
                    //Fortsetter å ta imot regnestykker fra Klient
                    regnestykke = leseren.readLine();
                }
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



