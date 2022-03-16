import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ServerImpl implements Server {
    String directory = null;
    HashMap<String, RequestHandler> requestHandlers= new HashMap<>();
    FileRequestHandler fileRequestHandler = null;

    @Override
    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    RequestData requestData = parseClient(client);

                    if(requestHandlers.get(requestData.path()) != null){
                        RequestHandler requestHandler = requestHandlers.get(requestData.path());
                        requestHandler.handleRequest(requestData);
                    } else {
                        fileRequestHandler.handleRequest(requestData);
                    }
                }
            }
        }
    }

    private RequestData parseClient(Socket client) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder requestBuilder = new StringBuilder();
        String line;

        while (!(line = bufferedReader.readLine()).isBlank()) {
            requestBuilder.append(line + "\r\n");
        }

        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] firstRequestLine = requestsLines[0].split(" ");
        String method = firstRequestLine[0];
        String path = firstRequestLine[1];
        String version = firstRequestLine[2];
        String host = requestsLines[1].split(" ")[1];

        List<String> headers = new ArrayList<>(Arrays.asList(requestsLines).subList(2, requestsLines.length));

        RequestData requestData = new RequestData(client,method, path, version, host, headers);

        System.out.println(requestData);

        return requestData;
    }

    @Override
    public void useStatic(String path) throws IOException {
        fileRequestHandler = new FileRequestHandler(path);
    }

    @Override
    public void useRequestHandler(String path, RequestHandler requestHandler) {
        requestHandlers.put(path,requestHandler);
    }
}
