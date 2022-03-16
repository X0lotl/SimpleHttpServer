import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerMain {
    private static Server createServer() throws IOException {
        Server server = new ServerImpl();
        server.useRequestHandler("/main", new RequestHandler() {
            @Override
            public void handleRequest(RequestData requestData, Response response) throws IOException {
                byte[] body = Files.readAllBytes(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\static\\main.html"));

                response.send("200",body,requestData.headers());
            }
        });

        server.useRequestHandler("/ua", new RequestHandler() {
            @Override
            public void handleRequest(RequestData requestData, Response response) throws IOException {
                response.send("200","Glory To Ukraine".getBytes(),requestData.headers());
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