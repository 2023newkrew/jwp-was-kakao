package view;

import webserver.HttpResponse;

import java.io.IOException;
import java.util.Map;

public interface View {
    void render(Map<String, Object> models, HttpResponse httpResponse) throws IOException;
}
