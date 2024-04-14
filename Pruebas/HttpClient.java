import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpClient {
    public static void main(String[] args) {
        boolean continueSending = true;

        while (continueSending) {
            try {
                // User inputs the URL
                System.out.println("Type the server URL you want to connect to (e.g., http://localhost:3000/path): ");
                String urlString = System.console().readLine();
                URL url = new URL(urlString);

                // Parsing URL components: extract host, port and path
                String server = url.getHost();
                int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
                String path = url.getPath().isEmpty() ? "/" : url.getPath();
                List<String> headers = new ArrayList<>();
                String method = "";
                String body = "";

                // User selects the HTTP method
                do {
                    System.out.println("Type the HTTP method you want to use (GET, HEAD, PUT, POST, DELETE): ");
                    method = System.console().readLine().toUpperCase();
                } while (!method.matches("GET|HEAD|PUT|POST|DELETE"));

                // Handling request body
                if (method.equals("PUT") || method.equals("POST") || method.equals("DELETE")) {
                    System.out.println("Type the body you want to add: ");
                    body = System.console().readLine();
                    headers.add("Content-Length: " + body.getBytes().length);
                }

                // User adds custom headers
                String header;
                do {
                    System.out.println("Type a custom header in the format 'Name: Value' or type 'done' to finish:");
                    header = System.console().readLine();
                    if (!header.equalsIgnoreCase("done")) {
                        headers.add(header);
                    }
                } while (!header.equalsIgnoreCase("done"));

                // Building and sending the request
                try (Socket socket = new Socket(server, port);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    // Constructing the request
                    StringBuilder requestBuilder = new StringBuilder();
                    requestBuilder.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");
                    requestBuilder.append("Host: ").append(server).append("\r\n");
                    for (String hdr : headers) {
                        requestBuilder.append(hdr).append("\r\n");
                    }
                    requestBuilder.append("\r\n").append(body);

                    // Sending the request
                    System.out.println(requestBuilder.toString());

                    // Reading and printing the response
                    System.out.println("\nServer response:");
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                // Checking if the user wants to send another request
                System.out.println("\nDo you want to send another request? (yes/no): ");
                String decision = System.console().readLine();
                continueSending = "yes".equalsIgnoreCase(decision);

            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
