import java.io.IOException;
import java.nio.file.Path;


public class ServerMain {
    private static Server createServer() {
        Server server = new ServerImpl();

        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\hello.html"));
        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\meme.html"));
        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\html\\main.html"));
        server.useRequestHandler(Path.of("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\site\\imeges\\meme.jpg"));

        return server;
    }

    public static void main(String[] args) throws IOException {
        Server server = createServer();
        server.start(8000);
    }
}