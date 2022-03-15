import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerMain{
    private static Server createServer() {
        RequestHandler requestHandler = new RequestHandler();
        Server server = new Server() {
            @Override
            public void start(int port) throws IOException {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    while (true) {
                        try (Socket client = serverSocket.accept()){
                            requestHandler.handleRequest(client);
                        }
                    }
                }
            }

            @Override
            public void useRequestHandler(Path path) {
                requestHandler.addHandler(String.valueOf(path.getFileName()), String.valueOf(path));
            }
        };

        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\hello.html"));
        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\meme.html"));
        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\main.html"));
        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\imeges\\meme.jpg"));

        return server;
    }

    public static void main(String[] args) throws IOException {
        Server server = createServer();
        server.start(8000);
    }
}