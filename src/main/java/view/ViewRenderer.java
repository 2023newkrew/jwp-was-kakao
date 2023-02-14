package view;

import webserver.HttpResponse;

import java.io.IOException;

public interface ViewRenderer {
    void render(HttpResponse httpResponse) throws IOException;

    void render(HttpResponse httpResponse, Object context) throws IOException;
}
