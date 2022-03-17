import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerImpl implements Server {
    List<RequestHandlerPicker> requestHandlers = new ArrayList<>();
    FileRequestHandler fileRequestHandler = null;
    RequestHandlerFor404 requestHandlerFor404 = new RequestHandlerFor404();
    ClientParser clientParser = new ClientParser();

    @Override
    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    RequestData requestData = clientParser.parse(client);
                    Response response = new ResponseImpl(client);

                    boolean hasMatch = false;
                    if (!Objects.equals(requestData.path(), "/favicon.ico")){
                        for (int i = 0; i < requestHandlers.size(); i++) {
                            hasMatch = requestData.path().matches(requestHandlers.get(i).pattern());
                            if (hasMatch ) {
                                RequestHandler requestHandler = requestHandlers.get(i).requestHandler();
                                requestHandler.handleRequest(requestData, response);
                                break;
                            }
                        }

                        if (fileRequestHandler != null && !hasMatch) {
                            fileRequestHandler.handleRequest(requestData, response);
                        } else if (!hasMatch) {
                            requestHandlerFor404.handleRequest(requestData, response);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void useStatic(String path) throws IOException {
        fileRequestHandler = new FileRequestHandler(path);
    }

    @Override
    public void useRequestHandler(String pattern, RequestHandler requestHandler) {
        requestHandlers.add(new RequestHandlerPicker(pattern, requestHandler));
    }
}
