package webserver;

import auth.HttpCookie;
import model.User;
import supports.HttpParser;
import supports.TemplateService;
import supports.UserService;
import utils.AuthUtils;
import utils.FileIoUtils;
import utils.MethodAndPathCheckUtils;
import utils.ResponseUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Arrays;

public class PathBinder {
    private static final String STATIC_ROOT_PATH = "./static";
    private static final String USER_LOGIN_FAIL_PATH = "/user/login_failed.html";
    private static final String INDEX_PATH = "/index.html";

    private final UserService userService;
    private final TemplateService templateService;

    public PathBinder() {
        userService = new UserService();
        templateService = new TemplateService();
    }

    public void bind(OutputStream out, BufferedReader br, HttpParser httpParser) throws IOException, URISyntaxException {
        byte[] body = new byte[0];
        DataOutputStream dos = new DataOutputStream(out);

        String method = httpParser.getMethod();
        String path = httpParser.getPath();
        if (MethodAndPathCheckUtils.isGetAndStatic(method, path)) {
            body = bindStatics(httpParser, dos);
        }
        if (MethodAndPathCheckUtils.isGetAndTemplate(method, path)) {
            body = bindTemplates(httpParser, dos);
        }
        if (MethodAndPathCheckUtils.isPostAndCreate(method, path)) {
            bindCreateUser(httpParser, dos, br);
        }
        if (MethodAndPathCheckUtils.isPostAndLogin(method, path)) {
            bindLoginUser(httpParser, dos, br);
        }

        ResponseUtils.responseBody(dos, body);
    }

    private byte[] bindStatics(HttpParser httpParser, DataOutputStream dos) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(STATIC_ROOT_PATH + httpParser.getPath());
        ResponseUtils.responseOkHeader(dos, body.length, httpParser.getPath());
        return body;
    }

    private byte[] bindTemplates(HttpParser httpParser, DataOutputStream dos) throws IOException, URISyntaxException {
        byte[] body = templateService.createHtmlBody(httpParser);
        if (Arrays.equals(body, "index".getBytes())) {
            ResponseUtils.responseRedirectHeader(dos, INDEX_PATH);
        } else {
            ResponseUtils.responseOkHeader(dos, body.length, httpParser.getPath());
        }
        return body;
    }

    private void bindCreateUser(HttpParser httpParser, DataOutputStream dos, BufferedReader br) throws IOException {
        userService.saveUser(br, httpParser);
        ResponseUtils.responseRedirectHeader(dos, INDEX_PATH);
    }

    private void bindLoginUser(HttpParser httpParser, DataOutputStream dos, BufferedReader br) throws IOException {
        User user = userService.findAuthorizedUser(br, httpParser);

        if (user != null) {
            HttpCookie httpCookie = AuthUtils.makeHttpCookie(user);
            ResponseUtils.responseLoginHeader(dos, httpCookie);
        } else {
            ResponseUtils.responseRedirectHeader(dos, USER_LOGIN_FAIL_PATH);
        }
    }
}
