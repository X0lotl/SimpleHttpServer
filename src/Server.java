import java.io.IOException;
import java.util.regex.Pattern;

public interface Server{
    void start(int port) throws IOException;

    void useStatic(String path) throws IOException;

    void useRequestHandler(String pattern, RequestHandler requestHandler);
}
