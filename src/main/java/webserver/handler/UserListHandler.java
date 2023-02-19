package webserver.handler;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import webserver.common.HttpSessions;
import webserver.exception.InternalServerErrorException;
import webserver.request.Request;
import webserver.response.Response;

import java.io.IOException;
import java.util.Objects;

public class UserListHandler implements Handler {
    @Override
    public Response apply(Request request) {
        String sessionId = request.getSessionId();
        if (
            Objects.isNull(sessionId) ||
            Objects.isNull(HttpSessions.findValue(sessionId, "user"))
        ) {
            return Response.found(
                new byte[0],
                request.findRequestedFileType(),
                "/index.html"
            );
        }

        return generateUserListResponse(request);
    }

    private Response generateUserListResponse(Request request) {
        try {
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/templates");
            loader.setSuffix(".html");
            Handlebars handlebars = new Handlebars(loader);
            Template template = handlebars.compile("user/list");
            String result = template.apply(DataBase.findAll());
            return Response.ok(result.getBytes(), request.findRequestedFileType());
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
    }
}
