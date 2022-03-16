import java.io.IOException;
import java.net.Socket;

public interface RequestHandler {
    void handleRequest(RequestData requestData) throws IOException;
}

