import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerImpl implements Server {
    HashMap<String, RequestHandler> requestHandlers= new HashMap<>();
    FileRequestHandler fileRequestHandler = null;

    @Override
    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    RequestData requestData = parseClient(client);
                    Response response = new ResponseImpl(client);

                    if(requestHandlers.get(requestData.path()) != null){
                        RequestHandler requestHandler = requestHandlers.get(requestData.path());
                        requestHandler.handleRequest(requestData,response);
                    } else if (fileRequestHandler != null){
                        fileRequestHandler.handleRequest(requestData,response);
                    } else ()
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

        HashMap<String, String> headers = new HashMap<>();

        for (int i = 2; i < requestsLines.length; i++){
            String[] headersArray = requestsLines[i].split(": ");
            StringBuilder header = new StringBuilder();
            for (int j = 1; j < headersArray.length; j++){
                header.append(headersArray[j]);
            }
                headers.put(headersArray[0], header.toString());

        }

        RequestData requestData = new RequestData(method, path, version, host, headers);

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
