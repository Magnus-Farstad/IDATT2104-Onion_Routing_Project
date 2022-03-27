package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class for creating a connection with the REST SpringBoot API
 * It is essentially methods for sending GET- and POST requests to the backend
 */

public class APIService {

    /**
     * A method for receiving data from the backend API. Sends a GET-request and receives
     * either the public key for the generated RSA keypair, or a string with all the nodes
     * @param url The URL to the computer which hosts the server
     * @return a String of the data needed
     * @throws Exception
     */
    public static String apiGETRequest(String url) throws Exception {
        URL urlForGetRequest = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String readLine;
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            return String.valueOf(response);

        } else {
            throw new Exception("Could not connect");
        }
    }

    /**
     * A method for posting a node the backend server from which the client later gets it from
     * @param url The URL to the computer which hosts the server
     * @param port The portnr. to the given node
     * @param aesKey The AES key generated for this node
     * @param address The address to the given node
     * @return A response code from the server. If everything is successful 201 will be returned
     * @throws Exception
     */
    public static int apiPOSTNode(String url,  String port, String aesKey, String address) throws Exception {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        return nodeAction(http, "POST", port, aesKey, address);
    }

    /**
     * A method for posting a string the backend server. It is used to send the public RSA key,
     * and the final message coming originally from the client.
     * @param url The URL to the computer which hosts the server
     * @param RSAPublicKey The RSA public key that will be used to encrypt the AES keys
     * @return A response code from the server. If everything is successful 201 will be returned
     * @throws Exception
     */
    public static int apiPOSTString(String url, String RSAPublicKey) throws Exception {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        return StringAction(http, "POST", RSAPublicKey);
    }

    /**
     * A helping method for the apiPOSTNode method. Specifies which action is being used,
     * and it's here the data is being arranged and sent.
     * @param http The http url connection, created in the apiPOSTString method, to the server
     * @param action The action that specifies the POST request
     * @param port The portnr. to the given node
     * @param aesKey The AES key generated for this node
     * @param address The address to the given node
     * @return A response code from the server. If everything is successful 201 will be returned
     * @throws IOException
     */
    private static int nodeAction(HttpURLConnection http, String action, String port, String aesKey, String address) throws IOException {
        http.setRequestMethod(action);
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{  \"port\":\"" + port + "\"," +
                         "\"key\":\"" + aesKey +  "\"," +
                         "\"address\":\"" + address + "\" }";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);
        int responseCode = http.getResponseCode();

        http.disconnect();

        return responseCode;
    }

    /**
     * A helping method for the apiPOSTString method. Specifies which action is being used,
     * and it's here the data is being arranged and sent.
     * @param http The http url connection, created in the apiPOSTString method, to the server
     * @param action The action that specifies the POST request
     * @param string The string that are being posted to the server
     * @return A response code from the server. If everything is successful 201 will be returned
     * @throws IOException
     */
    private static int StringAction(HttpURLConnection http, String action, String string) throws IOException {
        http.setRequestMethod(action);
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{  \"string\":\"" + string + "\" }";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);
        int responseCode = http.getResponseCode();

        http.disconnect();

        return responseCode;
    }
}
