package servlet;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import exception.BadRequestException;
import http.ContentType;
import http.HttpStatus;
import http.Uri;
import http.request.Request;
import http.request.RequestStartLine;
import http.response.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class ResourceServlet implements Servlet {
    private static final Logger logger = LoggerFactory.getLogger(ResourceServlet.class);

    private static class ResourceServletHolder {
        private static final ResourceServlet instance = new ResourceServlet();
    }

    private ResourceServlet() {
    }

    public static ResourceServlet getInstance() {
        return ResourceServletHolder.instance;
    }

    @Override
    public Response doGet(Request request) {
        RequestStartLine startLine = request.getStartLine();
        ContentType contentType = ContentType.of(startLine.getUri().getExtension().orElseThrow(BadRequestException::new));
        try {
            byte[] body = getResource(startLine.getUri(), contentType);
            return Response.builder()
                    .httpVersion(startLine.getVersion())
                    .httpStatus(HttpStatus.OK)
                    .contentType(contentType)
                    .contentLength(body.length)
                    .body(body)
                    .build();
        } catch (IOException | URISyntaxException | NullPointerException e) {
            throw new BadRequestException();
        }
    }

    private byte[] getResource(Uri uri, ContentType contentType) throws IOException, URISyntaxException {
        if (contentType.equals(ContentType.HTML)) {
            return getTemplateResources(uri);
        }
        return getStaticResource(uri);
    }

    private static byte[] getTemplateResources(Uri uri) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix("");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile(uri.getPath());

        List<User> users = DataBase.findAll().stream().toList();
        String resource = template.apply(Map.of("users", users));
        return resource.getBytes();
    }

    private byte[] getStaticResource(Uri uri) throws IOException, URISyntaxException {
        return FileIoUtils.loadFileFromClasspath("static" + uri.getPath());
    }


}
