package webserver.controller.support;

import com.github.jknack.handlebars.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.Request;
import webserver.http.response.Response;

public class TemplateView {
    private Template template;

    public TemplateView(Template template) {
        this.template = template;
    }

    public void render(Request request, Response response) throws Exception {
        byte[] body = template.apply(request.getAttributes()).getBytes();
        response.setContentType("text/html");
        response.setBody(body);
    }
}
