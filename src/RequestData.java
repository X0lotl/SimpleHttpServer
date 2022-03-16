import java.util.Map;

public record RequestData(String method, String path, String version, Map<String, String> headers) {
}
