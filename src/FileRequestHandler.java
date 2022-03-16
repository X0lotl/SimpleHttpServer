import java.io.*;
import java.nio.file.*;

public record FileRequestHandler(String pathOfDirectory) implements RequestHandler {

    @Override
    public void handleRequest(RequestData requestData, Response response) throws IOException {

        Path path = Paths.get(pathOfDirectory);
        Path filePath = path.resolve(requestData.path().substring(1));
        File file = new File(String.valueOf(filePath));

        if (file.exists()) {
            response.send("200", Files.readAllBytes(filePath), requestData.headers());
        } else {
            RequestHandlerFor404 requestHandlerFor404 = new RequestHandlerFor404();
            requestHandlerFor404.handleRequest(requestData,response);
        }
    }
}
