import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;

public interface RequestHandlerInterface {
    String guessContentType(Path filePath) throws IOException;

    void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException;

    void handleRequest(Socket client) throws IOException;

    File getFileFromHashMap(String path);

    void addHandler(String pathInServer, String path);

}

