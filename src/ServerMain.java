import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class ServerMain {
    private static Server createServer() throws IOException {
        Server server = new ServerImpl();
        server.useRequestHandler(".ua", new RequestHandler() {
            @Override
            public void handleRequest(RequestData requestData, Response response) throws IOException {
                requestData.headers().put("Content-Type", "text");
                response.send("200","Glory to Ukraine".getBytes(),requestData.headers());
            }
        });

        server.useStatic("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\static");

        return server;
    }

    public static void main(String[] args) throws IOException {
        Server server = createServer();
        server.start(8000);
    }
}