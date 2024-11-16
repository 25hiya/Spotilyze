import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import main.java.APITokenGetterClass;


public class serverSetup {

    private static final int PORT = 3000;
    private static final String REDIRECT_URI_PATH = "/callback"; // Use this path in the redirect URI

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(REDIRECT_URI_PATH, serverSetup::handleAuthorizationResponse);
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server is listening on http://localhost:" + PORT + REDIRECT_URI_PATH);
        }

    private static void handleAuthorizationResponse(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();

        // Extract the authorization code from the query string
        String code = null;
        if (query != null && query.contains("code=")) {
            code = query.split("code=")[1];
        }

        if (code != null) {
            // Here, call getToken(code) with the extracted code
            // In real implementation, call your token exchange logic here
            try {
                LoginClass.getToken(code);
                System.out.println("Authorization code received: " + code);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        String response = "Authorization received. You can close this window.";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
