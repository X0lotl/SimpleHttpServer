import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileRequestHandler implements RequestHandler {
    private final String pathOfDirectory;
    public FileRequestHandler(String pathOfDirectory){
        this.pathOfDirectory = pathOfDirectory;
    }

    public String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

    @Override
    public void handleRequest(RequestData requestData) throws IOException {

        HashMap<String, byte[]> hashMapForContent = new HashMap<>();
        Response response = new ResponseImpl();

        Path path = Paths.get(pathOfDirectory);//FileSystems.getDefault().getPath(pathOfDirectory);
        Path filePath = path.resolve(requestData.path().substring(1));
        File file = new File(String.valueOf(filePath));

        if(file.exists()){
            String contentType = guessContentType(Path.of(file.getPath()));
            hashMapForContent.put(contentType,Files.readAllBytes(Path.of(file.getPath())));
            response.send("200",hashMapForContent,requestData);
        } else {
            byte[] notFoundContent = "<h1>404 Not found :(</h1>".getBytes();
            hashMapForContent.put("text/html",notFoundContent);
            response.send("404",hashMapForContent,requestData);
        }

    }

    public String getFileName(Path path) {
        return "/" + String.valueOf(path.getFileName());
    }

}
