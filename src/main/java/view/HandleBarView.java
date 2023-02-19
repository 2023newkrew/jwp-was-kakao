package view;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import exception.ErrorCode;
import exception.WasException;
import webserver.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 템플릿 엔진을 활용하여 데이터를 가공함
 */
public class HandleBarView implements View {

    private final String url;
    private final Handlebars handlebars;

    public HandleBarView(String url) {
        this.url = url;
        this.handlebars = new Handlebars(new ClassPathTemplateLoader("/templates", ".html"));
    }

    @Override
    public void render(Map<String, Object> models, HttpResponse httpResponse) {
        try {
            Template template = handlebars.compile(url);

            byte[] templateBody = template.apply(models).getBytes(StandardCharsets.UTF_8);
            httpResponse.setResponseBody(templateBody);
        } catch (IOException e) {
            throw new WasException(ErrorCode.CAN_NOT_READ_DATA);
        }
    }
}
