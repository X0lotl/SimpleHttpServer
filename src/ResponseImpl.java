import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ResponseImpl implements Response {
    @Override
    public void send(String status, HashMap<String,Object> hashMapForContent, RequestData requestData) throws IOException {
        Socket client = requestData.client();
        String contentType = (String) hashMapForContent.get(hashMapForContent.keySet().toArray()[0]);
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write((byte[]) hashMapForContent.get(contentType));
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        clientOutput.close();
    }
}
