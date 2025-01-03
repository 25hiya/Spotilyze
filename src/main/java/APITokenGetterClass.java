package main.java;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class APITokenGetterClass {

  public static String accessToken = "BQDawSNAXJdp-cKCZunTFr__kVKfRE6cvmwx3KN_R0QqXu7pxAHKd7Wh1Re9i106lPyGbEiLOuZ6dThvw8WN6T3msw3DJk8Wh6GIlnWoAOcMCLDEeVM";
  public static String clientId = "16b096a1941b433cbb6e44973a7fc00c";  // Replace with your actual Client ID
  public static String clientSecret = "1a0e9d11b95d453ab5e030db0c734990";  // Replace with your actual Client Secret
  public static String redirectUri = "http://localhost:3000/callback";

  public static void main(String[] args) {

    try {
      // Create a client for sending HTTP requests
      HttpClient client = HttpClient.newHttpClient();

      // Prepare the authorization header by encoding the client ID and secret
      String authValue = clientId + ":" + clientSecret;
      String encodedAuth = Base64.getEncoder().encodeToString(authValue.getBytes(StandardCharsets.UTF_8));

      // Debugging: print the encoded authorization header
      System.out.println("Encoded Authorization: " + encodedAuth);

      // Set up the request body (URLEncode the form parameters)
      String requestBody = "grant_type=" + URLEncoder.encode("client_credentials", StandardCharsets.UTF_8);

      // Create the HTTP request
      HttpRequest request = HttpRequest.newBuilder()
              .uri(new URI("https://accounts.spotify.com/api/token"))
              .header("Authorization", "Basic " + encodedAuth)
              .header("Content-Type", "application/x-www-form-urlencoded")
              .header("Accept", "application/json")  // Accept JSON response
              .POST(HttpRequest.BodyPublishers.ofString(requestBody))
              .build();

      // Send the request and handle the response
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Check if the response code is 200 (OK)
      if (response.statusCode() == 200) {
        // Successfully received a token
        String responseBody = response.body();
        String token = extractAccessToken(responseBody);
        System.out.println("Access Token: " + token);
      } else {
        // Print error information
        System.out.println("Error: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Method to extract the access token from the JSON response
  private static String extractAccessToken(String responseBody) {
    // In a real application, you should use a JSON library like Jackson or Gson to parse the response
    // For simplicity, let's just extract the token with a basic approach
    String tokenPrefix = "\"access_token\":\"";
    int tokenStart = responseBody.indexOf(tokenPrefix) + tokenPrefix.length();
    int tokenEnd = responseBody.indexOf("\"", tokenStart);
    return responseBody.substring(tokenStart, tokenEnd);
  }
  public static HashMap getAuthToken(){
    HashMap returnDict = new HashMap();
    returnDict.put("Authorization", "Bearer" + accessToken);
    return returnDict;
  }
}
