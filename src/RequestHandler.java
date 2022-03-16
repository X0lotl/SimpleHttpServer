import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestHandler implements RequestHandlerInterface{
    HashMap<String, File> hashMapForFilesFromPath = new HashMap<>();

    @Override
    public String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

    @Override
    public void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException {
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        clientOutput.close();
    }

    @Override
    public void handleRequest(Socket client) throws IOException {
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
        String path = firstRequestLine[1];//.replace("/", "");
        String version = firstRequestLine[2];
        String host = requestsLines[1].split(" ")[1];

        List<String> headers = new ArrayList<>(Arrays.asList(requestsLines).subList(2, requestsLines.length));

        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                client.toString(), method, path, version, host, headers.toString());
        System.out.println(accessLog);


        if (getFileFromHashMap(path) != null) {
            File file = getFileFromHashMap(path);
            String contentType = guessContentType(Path.of(file.getPath()));
            sendResponse(client, "200 OK", contentType, Files.readAllBytes(Path.of(file.getPath())));
        } else {
            byte[] notFoundContent = "<h1>404 Not found :(</h1>".getBytes();
            sendResponse(client,"404 Not Found", "text/html", notFoundContent);
        }

    }

    @Override
    public File getFileFromHashMap(String path) {
        return hashMapForFilesFromPath.get(path);
    }

    @Override
    public String getFileName(Path path) {
        return "/" + String.valueOf(path.getFileName());
    }

    @Override
    public void addFileToHashMap(Path path) {
        hashMapForFilesFromPath.put(getFileName(path), new File(String.valueOf(path)));
    }
}
