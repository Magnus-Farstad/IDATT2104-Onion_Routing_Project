import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;

        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.print("Oppgi navnet på maskinen der tjenerprogrammet kjører: ");
        String tjenermaskin = leserFraKommandovindu.nextLine();

        Socket forbindelse = new Socket(tjenermaskin, PORTNR);
        System.out.println("Nå er forbindelsen opprettet.");

        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        String innledning1 = leseren.readLine();
        String innledning2 = leseren.readLine();
        System.out.println(innledning1 + "\n" + innledning2);

        String enLinje = leserFraKommandovindu.nextLine();
        while (!enLinje.equals("")) {
            skriveren.println(enLinje);
            String respons = leseren.readLine();  // mottar respons fra tjeneren
            System.out.println("Fra tjenerprogrammet: " + respons);
            enLinje = leserFraKommandovindu.nextLine();
        }

        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
