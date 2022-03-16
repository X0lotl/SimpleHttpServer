import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;

public interface RequestHandler {
    String guessContentType(Path filePath) throws IOException;

    void handleRequest(Socket client) throws IOException;

    File getFileFromHashMap(String path);

    String getFileName(Path path);

    void addFileToHashMap(Path path);

}

