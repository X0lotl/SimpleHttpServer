import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class ResponseImpl implements Response {
    private Socket client;

    public ResponseImpl(Socket client){
        this.client = client;
    }

    @Override
    public void send(String status, byte[] body, Map<String, String> headers) throws IOException {
        OutputStream clientOutput = client.getOutputStream();
        String contentType = headers.get("Content-Type");

        if (contentType == null){
            contentType = "text/html";
        }

        clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(body);
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        clientOutput.close();
    }
}
