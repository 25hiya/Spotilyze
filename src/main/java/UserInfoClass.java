
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
        String token = "BQBV9gwcYrTJ5ze_am1Vi0JwpK7iUww_iNf2K_Ue9cQKUtm32y7C9nJ3GyIo2bIeTziZ2xEtxua1fRUV3f59lp3Klqe6QHIzeT2977ewuutZNx07dEsKUOdKJI82uSqphQ4UVp9FtRrrJDGbs1bo_NlzZbcueOjcFUHfs6jazKZO8zl9ljYd5RnHhGHtWZNQc-8cFtA6eJsZtBMfs1U3QVYhCudjyg";
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
            String userName = "users/8uzkpaury9t8y6w8unavjex3s"; // Replace with the user's name
            searchUser(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
