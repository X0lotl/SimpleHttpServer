import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class ClientParser {
    public RequestData parse(Socket client) throws IOException {
        HashMap<String, String> headers = new HashMap<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null && !(line.isBlank())) {
            if (line != null) {
                requestBuilder.append(line + "\r\n");
            }
        }
        String request = requestBuilder.toString();

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
}
