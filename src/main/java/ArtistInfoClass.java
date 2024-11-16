
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.APITokenGetterClass;

public class ArtistInfoClass {

    private static final String SEARCH_URL = "https://api.spotify.com/v1/search";

    public static void searchArtist(String artistName) throws IOException {
        // Construct the query URL
        String token = "BQAUA8I2FTigShCCQH2K058Zk_Ym6UeDAHWGs8lBp1mEjrrUkdXWl1jz_LWfhu14KysCP-7DjWpOMPlJMHSR3Vh2H2IgB-XvE-jj4KTyWOYj7WJK3qXLLNQwRsN-SDadyf4UwkLnexbdl2kByNngHkR3AtT4cgnF0lmqWD4P6iLg7Nb0SmbLxgk7u46h0QMryA9lgXzP6glBbrd5qwYdHEY_PkbnRw";
        String query = "?q=" + artistName + "&type=artist&limit=1";
        String queryUrl = SEARCH_URL + query;

        // Set up the connection
        URL url = new URL(queryUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);

        // Handle response
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP Error: " + status);
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
            String artistName = "tupac"; // Replace with the artist's name
            searchArtist(artistName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*public class ArtistInfoClass {
    public void searchArtist(String artistName, String token) throws MalformedURLException {
        String url = "https://api.spotify.com/v1/search";
        HashMap headers = APITokenGetterClass.getAuthToken();
        String query = "?q=" + artistName + "&type=artist&limit=1";
        String queryUrl = url + query;

        URL finalurl = new URL(queryUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);

    }
}*/
