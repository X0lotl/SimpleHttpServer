import java.io.IOException;

public interface RequestHandler {
    void handleRequest(RequestData requestData, Response response) throws IOException;
}

