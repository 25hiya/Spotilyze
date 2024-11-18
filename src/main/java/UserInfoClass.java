
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.APITokenGetterClass;

public class UserInfoClass {

    private static final String SEARCH_URL = "https://api.spotify.com/v1/";

    public static void searchUser(String userName) throws IOException {
        // Construct the query URL
        String token = "BQB2X18TWo-yKhrm7tBYVyLlcDGFY_TejRyA1AL_MnZu0wO67SNbZ9do0EUYck7G1BXrW4R4tOElBx7t4i_faNXipVK6mRtZyHF1xWMcKKlJYmPrlJ20Ptq53oP21_HWbhvtJDqKJTjUCVak-luRZYP60_aWOMYl-JOSOUIMg2NcdGYcNIIG4T8rdI8Zze3J2BP00qqL4k_2kHOsoWUfQ4894a96dw";
        String queryUrl = SEARCH_URL + userName;

        //https://api.spotify.com/v1/me

        // Set up the connection
        URL url = new URL(queryUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);

        // Handle response
        int status = connection.getResponseCode();
        String message = connection.getResponseMessage();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP Error: " + status + " " + message);
        }

        // Read the JSON response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResult = mapper.readTree(response.toString());

        // Print the JSON response
        System.out.println(jsonResult.toPrettyString());
    }

    public static void main(String[] args) {
        try { // Replace with actual token from getToken()
            String userName = "me"; // Replace with the user's name
            searchUser(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
