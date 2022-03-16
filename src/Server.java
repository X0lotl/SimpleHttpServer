import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public interface Server{
    void start(int port) throws IOException;

    void useStatic(String path) throws IOException;

    void useRequestHandler(String path, RequestHandler requestHandler);
}
