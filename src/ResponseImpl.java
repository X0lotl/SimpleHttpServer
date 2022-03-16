import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ResponseImpl implements Response {
    private Socket client;

    public ResponseImpl(Socket client){
        this.client = client;
    }

    @Override
    public void send(String status, HashMap<String,byte[]> hashMapForContent, RequestData requestData) throws IOException {
        String contentType = (String) hashMapForContent.keySet().toArray()[0];
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
        clientOutput. write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(hashMapForContent.get(contentType));
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        clientOutput.close();
    }
}
