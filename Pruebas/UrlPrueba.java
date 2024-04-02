package Pruebas;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Scanner;
import java.net.URL;

public class UrlPrueba {
    public static void main(String[] args) throws MalformedURLException {
        Scanner scanner = new Scanner(System.in);
        
        // Change the server to a server wanted by the user
        System.out.println("Type the server you want to connect to: ");
        String server = scanner.nextLine();
        URL urlOfServer = new URL(server);

        int port =  urlOfServer.getPort() == -1 ? urlOfServer.getDefaultPort() : urlOfServer.getPort();
        String method="";
        String body = "";
        do{
            do{
                System.out.println("Type the HTTP method you want to use (GET, HEAD, PUT, POST, DELETE or EXIT): ");
                method = scanner.nextLine();
            }while(!method.equals("GET") && !method.equals("HEAD") && !method.equals("PUT") && !method.equals("POST") && !method.equals("DELETE") && !method.equals("EXIT"));
            

            if(method.equals("GET") || method.equals("HEAD")){
                System.out.println("Type the body you want to add: ");
                body = scanner.nextLine();
            }
            if(method.equals("PUT") || method.equals("DELETE")){
                System.out.println("Type the file you want to add (String): ");
                body = scanner.nextLine();
            }
            if(method.equals("POST")){
                System.out.println("Type two integers separated by a space to sum: ");
                body = scanner.nextLine();
            }
            
            String request = method + " / HTTP/1.1\r\n"
                        + "Host: " + server + "\r\n"
                        + "Connection: close\r\n" 
                        + "Content-Length: "+ body.getBytes().length +" \r\n"
                        + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                        + body+ "\r\n";
            
            try (Socket socket = new Socket(server, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                
                out.println(request);
                //System.out.println("Request sent to the server/n " + request);
                
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while(!method.equals("EXIT"));
        
        scanner.close();
    }
}
