import java.io.IOException;

public class RequestHandlerFor404 implements RequestHandler{
    @Override
    public void handleRequest(RequestData requestData, Response response) throws IOException {
        byte[] notFoundContent = "<h1>404 Not found :(</h1>".getBytes();
        requestData.headers().put("ContentType","text/html");
        response.send("404", notFoundContent, requestData.headers());
    }
}
