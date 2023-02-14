package supports;

import exception.NoSuchTemplateException;
import utils.AuthUtils;
import auth.SessionManager;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class TemplateService {
    private static final String TEMPLATE_ROOT_PATH = "./templates";
    private static final String HTML = ".html";
    private static final String USER_LIST_URL = "/user/list";
    private static final String USER_PROFILE_URL = "/user/profile";
    private static final String USER_LOGIN_PATH = "/user/login.html";

    public TemplateService() {
    }

    public byte[] createHtmlBody(HttpParser httpParser) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + httpParser.getPath());

        if (httpParser.getPath().startsWith(USER_LOGIN_PATH) && !AuthUtils.checkInvalidSession(httpParser.getCookie())) {
            body = "index".getBytes();
        }
        if (httpParser.getPath().startsWith(USER_PROFILE_URL)) {
            body = getUserProfileHtml(httpParser);
        }
        if (httpParser.getPath().startsWith(USER_LIST_URL)) {
            body = getUserListHtml(httpParser);
        }

        return body;
    }

    private byte[] getUserProfileHtml(HttpParser httpParser) throws IOException, URISyntaxException {
        if (AuthUtils.checkInvalidSession(httpParser.getCookie())) {
            return FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + USER_LOGIN_PATH);
        }
        Handlebars handlebars = initHandlebars();

        Template template = handlebars.compile(USER_PROFILE_URL);
        checkTemplatePath(template);

        String profileHtml = template.apply(SessionManager.findSession(httpParser.getCookie()).getAttribute("userObject"));

        return profileHtml.getBytes();
    }

    private byte[] getUserListHtml(HttpParser httpParser) throws IOException, URISyntaxException {
        if (AuthUtils.checkInvalidSession(httpParser.getCookie())) {
            return FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + USER_LOGIN_PATH);
        }
        Handlebars handlebars = initHandlebars();

        Template template = handlebars.compile(USER_LIST_URL);
        checkTemplatePath(template);

        String listHtml = template.apply(DataBase.findAll());

        return listHtml.getBytes();
    }

    private static Handlebars initHandlebars() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(HTML);
        return new Handlebars(loader);
    }

    private void checkTemplatePath(Template template) {
        if (template == null) {
            throw new NoSuchTemplateException();
        }
    }
}
