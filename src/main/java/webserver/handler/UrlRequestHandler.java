package webserver.handler;

public interface UrlRequestHandler extends Handler{
    String getRequestUrlRegex();
}
