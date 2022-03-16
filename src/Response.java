import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public interface Response {

    void send(String status, byte[] body, Map<String, String> headers) throws IOException;
}