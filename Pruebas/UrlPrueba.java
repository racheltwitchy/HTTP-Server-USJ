package Pruebas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UrlPrueba {
    public static void main(String[] args) {
        System.out.println("Type the server you want to connect to: ");
        String server = System.console().readLine();
        int port = 3000;
        String method = "";
        String body = "";
        String urlPath = ""; // Variable para guardar la ruta de la URL

        do {
            System.out.println("Type the URL path you want to request (e.g., /index.html): ");
            urlPath = System.console().readLine(); // Captura la ruta de la URL

            do {
                System.out.println("Type the HTTP method you want to use (GET, HEAD, PUT, POST, DELETE or EXIT): ");
                method = System.console().readLine();
            } while (!method.equals("GET") && !method.equals("HEAD") && !method.equals("PUT") && !method.equals("POST")
                    && !method.equals("DELETE") && !method.equals("EXIT"));

            if (method.equals("GET") || method.equals("HEAD")) {
                System.out.println("Type the body you want to add: ");
                body = System.console().readLine();
            }
            if (method.equals("PUT") || method.equals("DELETE")) {
                System.out.println("Type the file you want to add (String): ");
                body = System.console().readLine();
            }
            if (method.equals("POST")) {
                System.out.println("Type two integers separated by a space to sum: ");
                body = System.console().readLine();
            }

            String request = method + " " + urlPath + " HTTP/1.1\r\n"
                    + "Host: " + server + "\r\n"
                    + "Connection: close\r\n"
                    + "Content-Length: " + body.getBytes().length + " \r\n"
                    + "Date: " + java.time.LocalDateTime.now() + "\r\n"
                    + body + "\r\n";

            try (Socket socket = new Socket(server, port);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(request);

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!method.equals("EXIT"));
    }
}
