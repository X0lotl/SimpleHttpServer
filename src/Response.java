import java.io.IOException;
import java.util.HashMap;

public interface Response {

    void send(String status,HashMap<String,byte[]> hashMapForContent, RequestData requestData) throws IOException;
}
