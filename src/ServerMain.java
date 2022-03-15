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

public class ServerMain {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    handleClient(client);
                }
            }
        }
    }

    private static void handleClient(Socket client) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder requestBuilder = new StringBuilder();
        String line;

        while (!(line = bufferedReader.readLine()).isBlank()) {
            requestBuilder.append(line + "\r\n");
        }

        String request = requestBuilder.toString();

        String[] requestsLines = request.split("\r\n");
        String[] firstRequestLine = requestsLines[0].split(" ");
        String method = firstRequestLine[0];
        String path = firstRequestLine[1];
        String version = firstRequestLine[2];
        String host = requestsLines[1].split(" ")[1];

        List<String> headers = new ArrayList<>(Arrays.asList(requestsLines).subList(2, requestsLines.length));

        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                client.toString(), method, path, version, host, headers.toString());
        System.out.println(accessLog);

        Path filePath = getFilePath(path);

        if (Files.exists(filePath)) {
            String contentType = guessContentType(filePath);
            sendResponse(client, "200 OK", contentType, Files.readAllBytes(filePath));
        } else {
            byte[] notFoundContent = "<h1>404 Not found :(</h1>".getBytes();
            sendResponse(client,"404 Not Found", "text/html", notFoundContent);
        }
    }

    private static void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException {
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        clientOutput.close();
    }

    private static Path getFilePath(String path) {
        if ("/hello".equals(path)) {
            path = "C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\hello.html";
        } else if ("/meme".equals(path)) {
            path = "C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\imeges\\meme.jpg";
        } else if ("/".equals(path)){
            path = "C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\main.html";
        }

        //return Paths.get("/tmp/www", path);
        return Paths.get(path);
    }

    private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }
}