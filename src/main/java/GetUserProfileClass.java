
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.APITokenGetterClass;

public class GetUserProfileClass {

    private static final String SEARCH_URL = "https://api.spotify.com/v1/users/";

    public static void searchUser(String userName) throws IOException {
        // Construct the query URL
        String token = "AQDEiq9C7vko-Sz6HLlfoHc9Tkb2nvQajf-M5K90TE44RA24Lmkr_ZBC7nqhhUM-AkwZHuryJr0kfhD5rwq33qko91F40tkZNpldUddgsYSq4BjAX3pzfsSDTnUooNHFGiD-o9kKnA-X9gxPUKUhHdjbcMIp2cyShBvtWb_fQUYklkTTfB45mt27G5taIERcDr6Agcn2YZBRB8YDNMAF4i1w7-MMe0Ac9yzVGiow8FKTrUC77kj8MnkWiHo1nd6k2NjYFvVDZS23xmtG7L-VL_CZ9A7mJPdfkQ";
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
            String userName = "Defne Eriş"; // Replace with the artist's name
            searchUser("me");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
