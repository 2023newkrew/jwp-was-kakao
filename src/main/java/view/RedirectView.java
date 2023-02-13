package view;

import webserver.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class RedirectView implements View {

    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    public void render(Map<String, Object> models, HttpResponse httpResponse) throws IOException {
        httpResponse.sendRedirect(url);
    }

}
