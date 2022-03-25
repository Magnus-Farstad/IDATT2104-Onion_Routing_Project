package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APIService {

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


    public static int apiPOSTNode(String url,  String port, String aesKey, String address) throws Exception {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        return nodeAction(http, "POST", port, aesKey, address);
    }

    public static int apiPOSTString(String url, String key) throws Exception {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        return StringAction(http, "POST", key);
    }


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

    private static int StringAction(HttpURLConnection http, String action, String key) throws IOException {
        http.setRequestMethod(action);
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{  \"string\":\"" + key + "\" }";


        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);
        int responseCode = http.getResponseCode();

        http.disconnect();

        return responseCode;
    }
}
