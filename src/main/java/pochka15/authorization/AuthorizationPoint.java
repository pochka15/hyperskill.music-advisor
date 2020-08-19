package pochka15.authorization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * A point from which there could be taken authorized client
 */
public class AuthorizationPoint {
    private final URI accessServerPath;
    private final AuthCodeSource authCodeSource;

    public AuthorizationPoint(URI accessServerPath, AuthCodeSource authCodeSource) {
        this.accessServerPath = accessServerPath;
        this.authCodeSource = authCodeSource;
    }

    public AuthorizedClient getAuthorizedClient(String clientId) {
        final String uri = accessServerPath +
            "/authorize?" +
            "client_id=" + clientId +
            "&redirect_uri=http://localhost:8080/" +
            "&response_type=code";
        System.out.println("use this link to request the access code:\n" + uri + "\nwaiting for code...");
        final String receivedCode = authCodeSource.authorizationCode(); // Blocking operation
        System.out.println("code received\n" +
                               "making http request for access_token...\n" +
                               "Success!");
        final String responseText = responseBodyFromTokenRequest(receivedCode, clientId);
        final JsonElement accessToken = JsonParser.parseString(responseText).getAsJsonObject().get("access_token");
        return new AuthorizedClient(accessToken == null ? "" : accessToken.getAsString());
    }

    /**
     * @return response body from the request for the "authorization code" (See Oauth2 authorization using "authorization code")
     */
    private String responseBodyFromTokenRequest(String code, String clientId) {
        String secretCode = "Not today";
//        System.out.println("Enter the secret code");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            secretCode = br.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        HttpRequest request = HttpRequest.newBuilder()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic " + Base64.getEncoder()
                .encodeToString((clientId + ":" + secretCode).getBytes()))
            .uri(URI.create(accessServerPath + "/api/token"))
            .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code" +
                                                          "&code=" + code +
                                                          "&redirect_uri=http://localhost:8080/"))
            .build();
        try {
            return HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
