import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ServerImpl implements Server {
    List<RequestHandlerPicker> requestHandlers = new ArrayList<>();
    // HashMap<String, RequestHandler> requestHandlers = new HashMap<>();
    FileRequestHandler fileRequestHandler = null;
    RequestHandlerFor404 requestHandlerFor404 = new RequestHandlerFor404();
    HashMap<String, String> headers = new HashMap<>();

    @Override
    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    RequestData requestData = parseClient(client);
                    Response response = new ResponseImpl(client);

                    // Object[] patterns = requestHandlers.keySet().toArray();

                    boolean hasMath = false;
                    if (!Objects.equals(requestData.path(), "/favicon.ico")){
                        for (int i = 0; i < requestHandlers.size(); i++) {
                            hasMath = requestData.path().matches(requestHandlers.get(i).pattern());
                            if (hasMath) {
                                RequestHandler requestHandler = requestHandlers.get(i).requestHandler();
                                requestHandler.handleRequest(requestData, response);
                                break;
                            }
                        }

                        if (fileRequestHandler != null && !hasMath) {
                            fileRequestHandler.handleRequest(requestData, response);
                        } else if (!hasMath) {
                            requestHandlerFor404.handleRequest(requestData, response);
                        }
                    }
                }
            }
        }
    }

    private RequestData parseClient(Socket client) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null && !(line.isBlank())) {
            if (line != null) {
                requestBuilder.append(line + "\r\n");
            }
        }
        String request = requestBuilder.toString();

        System.out.println(request);

        String[] requestsLines = request.split("\r\n");
        String[] firstRequestLine = requestsLines[0].split(" ");
        String method = firstRequestLine[0];
        String path = firstRequestLine[1];
        String version = firstRequestLine[2];

        for (int i = 1; i < requestsLines.length; i++) {
            String[] headersArray = requestsLines[i].split(": ");
            StringBuilder header = new StringBuilder();
            for (int j = 1; j < headersArray.length; j++) {
                header.append(headersArray[j]);
            }
            headers.put(headersArray[0], header.toString());

        }

        RequestData requestData = new RequestData(method, path, version, headers);

        System.out.println(requestData);


        return requestData;
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
