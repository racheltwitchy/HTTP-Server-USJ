import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) {
        ArrayList<Car> coches = new ArrayList<Car>();
        String response = "";

        System.out.println("Type the port you want: ");
        String port = System.console().readLine();
        
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port))) {
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
                        + coches.toString()
                        + "\r\n";
                    }
                    if(method.equals("HEAD")){
                        response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n"
                        + "Date: "+ java.time.LocalDateTime.now() + "\r\n";
                    }
                    if(method.equals("PUT")){
                        String[] parts = body.split(" ");
                        Car temp=new Car(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                        
                        if(!coches.contains(temp)){
                            coches.add(temp);
                            response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n"
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                            + "Added car: " + temp.toString() ;
                        }else{
                            response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n"
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n" 
                            + "The car already exists";
                        }
                    }
                    if(method.equals("POST")){
                        String[] numbers = body.split(" ");
                        double min=Double.min(Double.parseDouble(numbers[0]),Double.parseDouble(numbers[1]));
                        double max=Double.max(Double.parseDouble(numbers[0]),Double.parseDouble(numbers[1]));
                        ArrayList<Car> temp = new ArrayList<Car>();
                        for(Car c: coches){
                            if(c.price>=min && c.price<=max){
                                temp.add(c);
                            }
                        }
                        response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n" 
                        + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                        + "The car between the prices (" + min + " y " + max + ") are: \r\n"
                        + temp.toString();
                    }
                    if(method.equals("DELETE")){
                        String[] parts = body.split(" ");
                        Car temp=new Car(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                        if(coches.contains(temp)){
                            coches.remove(temp);
                            response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n" 
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                            + "Deleted car: " + temp.toString();
                        }else{
                            response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n" 
                            + "Date: "+ java.time.LocalDateTime.now() + "\r\n"
                            + "The car does not exist";
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
