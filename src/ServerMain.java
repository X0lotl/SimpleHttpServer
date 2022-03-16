import java.io.IOException;

public class ServerMain {
    private static Server createServer() throws IOException {
        Server server = new ServerImpl();
        server.useStatic("C:\\Users\\Xolotl\\Documents\\GitHub\\SimpleHttpServer\\src\\static");

        return server;
    }

    public static void main(String[] args) throws IOException {
        Server server = createServer();
        server.start(8000);
    }
}