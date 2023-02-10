package supports;

import auth.AuthUtils;
import auth.SessionManager;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class TemplateService {
    private static final Logger log = LoggerFactory.getLogger(TemplateService.class);
    private static final String TEMPLATE_ROOT_PATH = "./templates";
    private static final String HTML = ".html";
    private static final String USER_LIST_URL = "/user/list";
    private static final String USER_PROFILE_URL = "/user/profile";
    private static final String USER_LOGIN_PATH = "/user/login.html";
    public TemplateService() {
    }

    public byte[] createHtmlBody(HttpParser httpParser) throws IOException, URISyntaxException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(HTML);
        Handlebars handlebars = new Handlebars(loader);

        String path = httpParser.getPath();
        if(path.startsWith(USER_LIST_URL)){
            if(AuthUtils.checkInvalidSession(httpParser.getCookie())){
                return FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + USER_LOGIN_PATH);
            }

            Template template = handlebars.compile(USER_LIST_URL);
            if(template == null){
                throw new RuntimeException("경로에 템플릿이 없습니다.");
            }

            String listHtml = template.apply(DataBase.findAll());

            return listHtml.getBytes();
        }

        if(path.startsWith(USER_PROFILE_URL)){
            if(AuthUtils.checkInvalidSession(httpParser.getCookie())){
                return FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + USER_LOGIN_PATH);
            }
            Template template = handlebars.compile(USER_PROFILE_URL);
            if(template == null){
                throw new RuntimeException("경로에 템플릿이 없습니다.");
            }

            String profileHtml = template.apply(SessionManager.findSession(httpParser.getCookie()).getAttribute("userObject"));

            return profileHtml.getBytes();
        }

        return FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
    }
}
