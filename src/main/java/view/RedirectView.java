package view;

import webserver.HttpResponse;

import java.util.Map;

public class RedirectView implements View {

    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    public void render(Map<String, Object> models, HttpResponse httpResponse) {
        httpResponse.sendRedirect(url);
    }

}
