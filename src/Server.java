import java.io.IOException;

public interface Server{
    void start(int port) throws IOException;

    void useStatic(String path) throws IOException;

    void useRequestHandler(String path, RequestHandler requestHandler);
}
