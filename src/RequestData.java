import java.util.Map;

public record RequestData(String method, String path, String version, String host, Map<String, String> headers) {
}
