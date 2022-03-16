import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class ServerImpl implements Server{
    RequestHandler requestHandler = new RequestHandler();

    @Override
    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    requestHandler.handleRequest(client);
                }
            }
        }
    }

    @Override
    public void useRequestHandler(Path path) {
        requestHandler.addHandler(String.valueOf(path.getFileName()), String.valueOf(path));
    }
}
