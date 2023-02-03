package utils;

public class HttpParser {
    private final String httpRequest;

    public HttpParser(String httpRequest) {
        this.httpRequest = httpRequest;
    }

    public String getHttpMethod(){
        return httpRequest.split(" ")[0];
    }

    public String getPath(){
        return httpRequest.split(" ")[1];
    }

}
