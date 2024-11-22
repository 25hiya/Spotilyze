import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

import main.java.APITokenGetterClass;
import org.json.JSONObject;

public class LoginClass{

    private static final String CLIENT_ID = APITokenGetterClass.clientId; // Replace with actual client ID
    private static final String REDIRECT_URI = APITokenGetterClass.redirectUri; // Replace with actual redirect URI
    private static final String AUTHORIZATION_ENDPOINT = "https://accounts.spotify.com/authorize";
    private static final String TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token";
    private static final String SCOPE = "user-read-private user-read-email";

    private static String codeVerifier;

    public static void main(String[] args) throws Exception {
        serverSetup.main(args);
        if (getStoredToken("access_token") == null || isTokenExpired()) {
            if (getStoredToken("refresh_token") != null) {
                refreshToken();
            } else {
                redirectToSpotifyAuthorize();
                while (getStoredToken("access_token") == null) {}
                System.out.println("Recieved: " + getStoredToken("access_token"));
            }
        } else {
            String accessToken = getStoredToken("access_token");
            getUserData(accessToken);
        }
    }

    // Redirects user to Spotify authorization page
    private static void redirectToSpotifyAuthorize() throws Exception {
        codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        // Construct authorization URL
        String authUrl = AUTHORIZATION_ENDPOINT + "?response_type=code"
                + "&client_id=" + CLIENT_ID
                + "&scope=" + SCOPE
                + "&redirect_uri=" + REDIRECT_URI
                + "&code_challenge_method=S256"
                + "&code_challenge=" + codeChallenge;

        // Store code verifier
        storeToken("code_verifier", codeVerifier);

        // Redirect user (print URL for user to visit)
        System.out.println("Visit this URL to log in: " + authUrl);
    }

    // Gets token with the authorization code
    public static void getToken(String code) throws Exception {
        String codeVerifier = getStoredToken("code_verifier");

        String params = "client_id=" + CLIENT_ID
                + "&grant_type=authorization_code"
                + "&code=" + code
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                + "&code_verifier=" + codeVerifier;

        HttpURLConnection connection = (HttpURLConnection) new URL(TOKEN_ENDPOINT).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        // Parse the response
        if (connection.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            storeToken("access_token", jsonResponse.getString("access_token"));
            storeToken("refresh_token", jsonResponse.optString("refresh_token", null));
            System.out.println(jsonResponse.getString("access_token"));
        }
    }

    // Refreshes the access token using the refresh token
    private static void refreshToken() throws Exception {
        String refreshToken = getStoredToken("refresh_token");

        String params = "client_id=" + CLIENT_ID
                + "&grant_type=refresh_token"
                + "&refresh_token=" + refreshToken;

        HttpURLConnection connection = (HttpURLConnection) new URL(TOKEN_ENDPOINT).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        if (connection.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            storeToken("access_token", jsonResponse.getString("access_token"));

            int expiresIn = jsonResponse.getInt("expires_in");
            long expiryTime = System.currentTimeMillis() + (expiresIn * 1000);
            storeToken("expires", String.valueOf(expiryTime));
        }
    }

    // Checks if the token is expired
    private static boolean isTokenExpired() {
        String expiryTimeStr = getStoredToken("expires");
        if (expiryTimeStr == null) return true;

        long expiryTime = Long.parseLong(expiryTimeStr);
        return System.currentTimeMillis() >= expiryTime;
    }

    // Fetches user data using the access token
    private static void getUserData(String accessToken) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://api.spotify.com/v1/me").openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        if (connection.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            System.out.println("User Data: " + response.toString());
        }
    }

    // Generates a random code verifier
    private static String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] code = new byte[32];
        random.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    // Generates a SHA-256 code challenge based on the code verifier
    private static String generateCodeChallenge(String verifier) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(verifier.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    // Stores token data in a properties file (as a substitute for localStorage)
    private static void storeToken(String key, String value) {
        try (FileOutputStream fos = new FileOutputStream("tokens.properties")) {
            Properties props = new Properties();
            props.setProperty(key, value);
            props.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retrieves stored token data from properties file
    private static String getStoredToken(String key) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("tokens.properties")) {
            props.load(fis);
            return props.getProperty(key);
        } catch (IOException e) {
            return null;
        }
    }
}
