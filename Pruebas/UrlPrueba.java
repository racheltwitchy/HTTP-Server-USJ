package Pruebas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

public class UrlPrueba {
    public static void main(String[] args) {
        System.out.println("Type the server URL you want to connect to (e.g., http://localhost:3000/path): ");
        try {
            String urlString = System.console().readLine();
            URL url = new URL(urlString);

            String server = url.getHost();
            int port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort(); // Utiliza el puerto de la URL o el
                                                                                   // puerto por defecto del protocolo
                                                                                   // si no se especifica uno.
            String path = url.getPath().isEmpty() ? "/" : url.getPath(); // Asegura que el path no esté vacío.

            String method = "";
            String body = "";
            do {
                do {
                    System.out.println("Type the HTTP method you want to use (GET, HEAD, PUT, POST, DELETE or EXIT): ");
                    method = System.console().readLine();
                } while (!method.equals("GET") && !method.equals("HEAD") && !method.equals("PUT")
                        && !method.equals("POST") && !method.equals("DELETE") && !method.equals("EXIT"));

                if (method.equals("EXIT")) {
                    break; // Sale del bucle si el usuario elige EXIT.
                }

                if (method.equals("PUT") || method.equals("POST") || method.equals("DELETE")) {
                    System.out.println("Type the body you want to add: ");
                    body = System.console().readLine();
                }

                String request = method + " " + path + " HTTP/1.1\r\n"
                        + "Host: " + server + "\r\n"
                        + "Connection: close\r\n"
                        + "Content-Length: " + body.getBytes().length + "\r\n"
                        + "\r\n"
                        + body;

                try (Socket socket = new Socket(server, port == -1 ? 80 : port);
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
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
