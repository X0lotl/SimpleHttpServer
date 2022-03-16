import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestHandlerImpl implements RequestHandler {
    HashMap<String, File> hashMapForFilesFromPath = new HashMap<>();

    @Override
    public String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

    @Override
    public void handleRequest(Socket client) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        Response response = new ResponseImpl();

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

        RequestData requestData = new RequestData(client,method, path, version, host, headers);

        System.out.println(requestData);

        HashMap<String, Object> hashMapForContent = new HashMap<>();
        if (getFileFromHashMap(path) != null) {
            File file = getFileFromHashMap(path);
            String contentType = guessContentType(Path.of(file.getPath()));
            hashMapForContent.put(contentType,file);
            response.send("200",hashMapForContent,requestData);
        } else {
            byte[] notFoundContent = "<h1>404 Not found :(</h1>".getBytes();
            hashMapForContent.put("text/html",notFoundContent);
            response.send("404",hashMapForContent,requestData);
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
