import java.io.*;
import java.nio.file.*;

public class FileRequestHandler implements RequestHandler {
    private final String pathOfDirectory;

    public FileRequestHandler(String pathOfDirectory){
        this.pathOfDirectory = pathOfDirectory;
    }

    @Override
    public void handleRequest(RequestData requestData, Response response) throws IOException {

        Path path = Paths.get(pathOfDirectory);
        Path filePath = path.resolve(requestData.path().substring(1));
        File file = new File(String.valueOf(filePath));
        if (file.exists()) {
            requestData.headers().put("Content-Type", getContentType(filePath));
            response.send("200", Files.readAllBytes(filePath), requestData.headers());
        } else {
            RequestHandlerFor404 requestHandlerFor404 = new RequestHandlerFor404();
            requestHandlerFor404.handleRequest(requestData,response);
        }
    }

    private String getContentType(Path path) throws IOException {
        return Files.probeContentType(path);
    }
}
