import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

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
    public void useStatic(String path) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(requestHandler::addFileToHashMap);
        }
    }

    @Override
    public void useRequestHandler(Path path) {

    }
}
