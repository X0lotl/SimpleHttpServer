import java.net.Socket;
import java.util.List;

public record RequestData(String method, String path, String version, String host, List<String> headers) {
}
