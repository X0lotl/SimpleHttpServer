import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        int port = 8000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Server starts on port: " + port);

        while(true){
            Socket clientSocket = serverSocket.accept();
            System.err.println("New client connect");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String newString;
            while ((newString = bufferedReader.readLine()) != null){
                System.out.println(newString);
                if (newString.isEmpty()){
                    break;
                }
            }

            bufferedWriter.write("HelloWorld!");
            System.out.println("Client connection terminate");
            bufferedWriter.close();
            bufferedReader.close();
            clientSocket.close();
        }
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello World!";
            exchange.sendResponseHeaders(200,response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
}