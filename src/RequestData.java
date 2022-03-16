import java.net.Socket;
import java.util.List;
import java.util.Map;

public record RequestData(String method, String path, String version, String host, Map<String, String> headers) {
}
