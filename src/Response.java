import java.io.IOException;
import java.util.Map;

public interface Response {

    void send(String status, byte[] body, Map<String, String> headers) throws IOException;
}