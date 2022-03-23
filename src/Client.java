import cryptography.AESencryption;
import cryptography.RSAKeyPairGenerator;
import model.Node;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import static API.APIService.apiGETRequest;

public class Client {

    private static final String PRIVATE_KEY_1 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs6hMk1chReRa76VAVx42YDGAv+bGwLlxAZybLwA9pFuU2oj1T9pdDWQen4m/Yo0oOFQAKsd1FWnILPtdIU3Is++k4DYMCSEQK+FA4a1wZ5ghx1QiUcu/e08gl9nDtYyGZixZpGb8hM5PToyNTL1AqKDEPOvEEX+Lia9Hp9/7hVAgMBAAECgYAEmiSLrpj2P6FDQcqx6qF6Sccxu9ZNEb7lzE3tjy4eD4kh40h0lZyP53pGzIcbMjSjj5TJP4G+GJpFSpEybEnpjxPIljg42pEJNvpKLxbT86ZCM0WN4qfXL5dA5sWqY+laB59Td+fATnc1Nj0n8CL3/i6PpQwg01M5qoJTPgVZAQJBANmfWXcX88s+XuFB7hFjLGi0euCvMW/dXeETWk1CYQEkMGEG5+ElrGiLFiraO6CIdZ1nOQueD7RQtM5846taKMECQQC2ml+qFhCGzez8HK1cMXEgMNzM3aLhvUmp6fNNhqDSNSYzGy1K6NiJX1CE9yOa525M4nZ1Jh1vPnrws3BOBgCVAkEAmqCUywAj45fPhrJ327bhyQvj+12//MIHgHNlyFuP3WW/UlG71MgV9rpM5+nkUC5lk4/SgqSud+qYbddjVU9cgQJAEbh5gDAT+oERdoXx7Ph/Wfhj9R2tKOsNsweZLPTbtoqh4mPIyXQ/T1WIot64/ddnxN5VUJkaUilmFOXVCD1c4QJBAIaZyp6Q8HIyL7iwClaCAfu8mSt+aB+9s4avPzexFJrvfogunGaU62F6oV78LRsiDbe+6sjG0nGqnWDvkrSDpH0=";
    private static final String PUBLIC_KEY_1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbOoTJNXIUXkWu+lQFceNmAxgL/mxsC5cQGcmy8APaRblNqI9U/aXQ1kHp+Jv2KNKDhUACrHdRVpyCz7XSFNyLPvpOA2DAkhECvhQOGtcGeYIcdUIlHLv3tPIJfZw7WMhmYsWaRm/ITOT06MjUy9QKigxDzrxBF/i4mvR6ff+4VQIDAQAB";

    private static final String PUBLIC_KEY_2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBm/5WPfX3zkuQcBAL/DAW97h2OOLSoaLPeFkkuE4hSbS81bMLENAy+NOhd85QGqbDQC4F7wS3U5w1nVnnX6bjKaiinq1bsp56YEL81WZNcgI6PSvHHXxm8GoPQPTK5SVzbV0p4rDxf6cWV+tm6vHkYoArNNOhaEV12OHMQJjvEQIDAQAB";
    private static final String PRIVATE_KEY_2 ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIGb/lY99ffOS5BwEAv8MBb3uHY44tKhos94WSS4TiFJtLzVswsQ0DL406F3zlAapsNALgXvBLdTnDWdWedfpuMpqKKerVuynnpgQvzVZk1yAjo9K8cdfGbwag9A9MrlJXNtXSnisPF/pxZX62bq8eRigCs006FoRXXY4cxAmO8RAgMBAAECgYAAuBn0FOm/vvmmisoXpAXad9yuKFcClC6ncjrGk5Cs3pvTk1BWpTKxFbYuLES7st6FpV4+6i7IJxRuFNFty5cqLzcjA6nW7P6UmKekov+cgnyJl4znhSRMAVfPpQwfgKgB1QYw5oOxYeEY4wI+S2lo5vQnGEZ6U6c8pprjpc+iAQJBAIcZWhEE4kSPntU59Ckl7hjTjYvMsp0K4Bi11hanxpVuRiQ/NsuBk1RD3kPcEVlQnC53uw9Q9uBSUtxZanhUSCUCQQD1mPpLQ9XmFe4AXuia7MAs7NPMir4RuWxYFtse8b/sOH8/X2kfNx7ay0F5ytUr8zyu8DY+xXE5BmDr9eZoxlF9AkA4RLben8oeBDODW7/143ZnoLUzpO4/umfb4uBoTzjGxEcykaGg4TcbwaixWtde+9QRBo1Cs9YfWCpq3FMcCv6BAkBJUL2HMlzsLqe53Js7hGlp/9jKOrC6wcuiEFChUDGm5sa1uFm9Q8smLX8CSJaSZC49WuAWpQJDr6/HQMTjijBRAkBPgAgxln9QtBbqybFsWda74wlJ4kggyWA2LDLsnB9QcfdidZHRnek4IpSISmMU2XWtcGVQlDKCfUP+P+IiCYAB";

    private static final String PUBLIC_KEY_3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUZ/sz+OJ3dtjkNdb6FPxbF7VKuDHgCJy5PCEaeNiqkCqh3VwCJBRZ59VHi4k0xdvMVqa8WioJzd7tajNGRgaJiur8F/8cT3xLvefVmxKiIyD4ZUlWaA/9bZroGL0dUE9ze3R5zgczaU9SMJHWeYJJ/w7ckBc5xeIAsLv3slbrLwIDAQAB";
    private static final String PRIVATE_KEY_3 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANRn+zP44nd22OQ11voU/FsXtUq4MeAInLk8IRp42KqQKqHdXAIkFFnn1UeLiTTF28xWprxaKgnN3u1qM0ZGBomK6vwX/xxPfEu959WbEqIjIPhlSVZoD/1tmugYvR1QT3N7dHnOBzNpT1IwkdZ5gkn/DtyQFznF4gCwu/eyVusvAgMBAAECgYAK7CUQtMHpGS+pVSYA87t0Xbg8guWT9lH7sxVc/J+7pQwR5KEWGatKhtQL7UwLJVHRZKniuzfveUXk619+n0DJ/250WMG6Hv0VzuaHgFNCjPi7PWBuySVORGYqZuWAG9VsZCDmGEdgBAAtzay2MqwGx5PwJ6fmqG2Ko/HP1SL4JQJBAN5mhZrTo+oDkcH2aE8e2R909mMRczmBSTZhiBRgZOgwdIWcoIUQKvVrvCEMwz5ZNCCwkr6/wgYdFlh6hwpBel0CQQD0fuxup8KXF5is8XpRrxS0Wy8E7dPhWF1Y+nimwZ7GxhB+MMuJ3dnHDxQydMTiYuhag1A5Wigp+MKfaMSKKJr7AkAPqvdUnf5ZOSEmof5dPJYdQjctaYhNj88hlqNolBXnyaob05n3Zdkw6wMY7PZASTaD6wybhZTcq2Xsm80xqsU9AkEAx9FHd7Qhg4xkWctM1Z9KQ5BWICgixwOJ3uNtYZPSKM/MwOUuI7Gtf1MihY4LLp35GahCE21Mb+j/XnqoTeWbqwJAPPPFvxIZzbkJ08biNt0+Q1gqpsC9vFU75PQfSp0mXqTrd13TfVUQMRph7pXcQO3oXqAiAolw77H3/0Fjm1ioLw==";

    public static void main(String[] args) throws Exception {
        final int PORTNR1 = 1251;
        final int PORTNR2 = 1252;
        final int PORTNR3 = 1253;

        final RSAKeyPairGenerator keyPairGenerator1 = new RSAKeyPairGenerator();
        keyPairGenerator1.initFromStrings(PUBLIC_KEY_1, PRIVATE_KEY_1);

        final RSAKeyPairGenerator keyPairGenerator2 = new RSAKeyPairGenerator();
        keyPairGenerator2.initFromStrings(PUBLIC_KEY_2, PRIVATE_KEY_2);

        final RSAKeyPairGenerator keyPairGenerator3 = new RSAKeyPairGenerator();
        keyPairGenerator3.initFromStrings(PUBLIC_KEY_3, PRIVATE_KEY_3);


        /* Bruker en scanner til å lese fra kommandovinduet */
        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.print("Oppgi navnet på maskinen der tjenerprogrammet kjører: ");
        String tjenermaskin = leserFraKommandovindu.nextLine();

        /* Setter opp forbindelsen til tjenerprogrammet */
        Socket forbindelse = new Socket(tjenermaskin, 1250);
        System.out.println("Nå er forbindelsen opprettet.");

        /* �pner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        String response = apiGETRequest("http://localhost:8080/getNodes");
        System.out.println(response);
        String[] list = response.split(",");
        String aesKeyString = list[1];

        AESencryption aeSencryption = new AESencryption();
        SecretKey aesKey = aeSencryption.convertStringToSecretKeyto(aesKeyString);
        System.out.println("SECRETKEY: " + aesKey);



        /* Leser innledning fra tjeneren og skriver den til kommandovinduet */
        String innledning1 = leseren.readLine();
        System.out.println(innledning1);

        /* Leser tekst fra kommandovinduet (brukeren) */
        String enLinje = leserFraKommandovindu.nextLine();
        while (!enLinje.equals("")) {
            /*
            String encryptedMessage = keyPairGenerator1.encrypt(enLinje);
            encryptedMessage += PORTNR3;
            encryptedMessage = keyPairGenerator3.encrypt(encryptedMessage);
            encryptedMessage += PORTNR2;
            encryptedMessage = keyPairGenerator2.encrypt(encryptedMessage);
            encryptedMessage += PORTNR1;
            encryptedMessage = keyPairGenerator1.encrypt(encryptedMessage);

             */

            String encryptedMessage = aeSencryption.encrypt(enLinje, aesKey);
            skriveren.println(encryptedMessage);
            String respons = leseren.readLine();  // mottar respons fra tjeneren
            System.out.println(respons);
            enLinje = leserFraKommandovindu.nextLine();
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}