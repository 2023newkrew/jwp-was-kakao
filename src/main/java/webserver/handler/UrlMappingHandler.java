package webserver.handler;

public interface UrlMappingHandler extends Handler {
    String getUrlMappingRegex();
}
