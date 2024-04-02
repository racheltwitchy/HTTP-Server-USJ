import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class server {

    public static void main(String[] args) {
        int port = 3000;
        
        ArrayList<String> messages = new ArrayList<String>();
        String response = "";
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     OutputStream output = socket.getOutputStream();
                     PrintWriter writer = new PrintWriter(output, true)) {
                    
                    StringBuilder requestBuilder = new StringBuilder();
                    String line;
                    while (!(line = input.readLine()).isBlank()) {
                        requestBuilder.append(line + "\r\n");
                    }
                    
                    String request = requestBuilder.toString();
                    String method = request.split(" ")[0];
                    //int bodyIndex = request.indexOf("\r\n\r\n"); Doesnt work properly

                    //String body = request.substring(bodyIndex + 4); // Skip the "\r\n\r\n"
                    String body=request.split("\r\n")[request.split("\r\n").length-1];
                    if(method.equals("GET")){
                        response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n" +
                        "Echoing back your request body(GET):\r\n" 
                        + "Content-Length: "+ body.getBytes().length +" \r\n"
                        + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                        + messages.toString()
                        + "\r\n";
                    }
                    if(method.equals("HEAD")){
                        response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n"
                        + "Date: "+ java.time.LocalDateTime.now() + "\r\n";
                       
                    }
                    if(method.equals("PUT")){
                        if(!messages.contains(body)){
                            messages.add(body);
                            response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n"
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                            + "Added file: " + body ;
                        }else{
                            response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n"
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n" 
                            + "The file already exists";
                        }
                    }
                    if(method.equals("POST")){
                        String[] numbers = body.split(" ");
                        int sum = Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);
                        response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n" 
                        + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                        + "Sum: " + sum;
                        
                    }
                    if(method.equals("DELETE")){
                        if(messages.contains(body)){
                            messages.remove(body);
                            response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n" 
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                            + "Deleted file: " + body;
                        }else{
                            response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n" 
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                            + "The file does not exist";
                        }
                    }
                    writer.print(response);
                   
                }
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
