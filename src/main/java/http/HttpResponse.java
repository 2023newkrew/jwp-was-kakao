package http;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private static final Handlebars handlebars;
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String TEMPLATE_PATH = "./templates";
    private static final String STATIC_PATH = "./static";
    private static final String TEMPLATE_SUFFIX = "/templates";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String LOCATION = "Location";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String SET_COOKIE = "Set-Cookie";

    private ResponseStatus responseStatus = ResponseStatus.OK;
    private final Map<String, String> headers = new HashMap<>();
    private final CookieList cookies = CookieList.empty();
    private Object model;
    private byte[] body;

    static {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix(TEMPLATE_SUFFIX);
        loader.setSuffix("");
        handlebars = new Handlebars(loader);
    }

    public String getStatusLine() {
        return HTTP_VERSION + " " + responseStatus.toString();
    }

    public List<String> getHeaderStrings() {
        return headers.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.toList());
    }

    public byte[] getBody() {
        return body;
    }

    public void applyPath(String path) {
        headers.put(LOCATION, path);
        headers.put(CONTENT_TYPE, ContentType.of(path).getValue() + ";charset=utf-8");
        this.body = render(path);
        logger.info("Forward to file : {}", getResourcePath(path));
        headers.put(CONTENT_LENGTH, String.valueOf(body.length));
    }

    public void setCookie(Cookie cookie) {
        cookies.addCookie(cookie);
        headers.put(SET_COOKIE, cookies.toString());
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setModel(Object model) {
        this.model = model;
    }


    private String getResourcePath(String path) {
        if (path.endsWith(ContentType.TEXT_HTML.getExtension())) {
            return TEMPLATE_PATH + path;
        }
        return STATIC_PATH + path;
    }

    private byte[] render(String path) {
        if (model == null) {
            return FileIoUtils.loadFileFromClasspath(getResourcePath(path));
        }
        try {
            Template template = handlebars.compile(path);
            return template.apply(model).getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
