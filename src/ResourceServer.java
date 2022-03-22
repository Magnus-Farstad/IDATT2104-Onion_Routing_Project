import java.io.*;
import java.net.*;

public class ResourceServer {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1251;

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for tjenersiden. Nå venter vi...");
        while (true) {
            Socket forbindelse = tjener.accept();
            SocketThread socketThread = new SocketThread(forbindelse);
            socketThread.start();
        }
    }
}
