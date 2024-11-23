
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
        String token = "BQDISbeDTIF3tEFow2mL4V4fi3lEowhp6A5xfWFvfmBMcfRxXwKP_AxQcPBYfpgfGOprre9HDnROyT8_xzW-gSx1226k1vDVorqAISfRK0ZAkVKsB9CEvxQSeosze0qd01-DaQU7EfjVrXTb46sYF56qrDkHo0OH5UffsFKYHx47JrUEV59cPWFvpha6YT2aJgc412Wl9_PI6YrW9ZL57vO65Ixvrg";
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
            if (userName.equals("me")) {
                searchUser(userName);
            }
            else {
                searchUser("users/" + userName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
