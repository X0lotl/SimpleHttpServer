public class RequestHandlerManager{
    private String pattern;
    private RequestHandler requestHandler;

    public RequestHandlerManager(String pattern, RequestHandler requestHandler){
        this.pattern = pattern;
        this.requestHandler = requestHandler;
    }

    public String getPattern(){
        return pattern;
    }

    public RequestHandler getRequestHandler(){
        return requestHandler;
    }
}
