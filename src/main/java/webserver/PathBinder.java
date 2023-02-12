package webserver;

import auth.HttpCookie;
import model.User;
import supports.HttpParser;
import supports.TemplateService;
import supports.UserService;
import utils.AuthUtils;
import utils.FileIoUtils;
import utils.ResponseUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

public class PathBinder {
    private static final String STATIC_ROOT_PATH = "./static";
    private static final String HTML = "html";
    private static final String CSS = "/css";
    private static final String FONTS = "/fonts";
    private static final String IMAGES = "/images";
    private static final String JS = "/js";
    private static final String USER_CREATE_URL = "/user/create";
    private static final String USER_LOGIN_URL = "/user/login";
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
        if (Objects.equals(method, "GET") &&
                path.startsWith(CSS) ||
                path.startsWith(FONTS) ||
                path.startsWith(IMAGES) ||
                path.startsWith(JS)) {
            body = bindStatics(httpParser, dos);
        }
        if (Objects.equals(method, "GET") && path.endsWith(HTML)) {
            body = bindTemplates(httpParser, dos);
        }
        if (Objects.equals(method, "POST") && path.startsWith(USER_CREATE_URL)) {
            bindCreateUser(httpParser, dos, br);
        }
        if (Objects.equals(method, "POST") && path.startsWith(USER_LOGIN_URL)) {
            bindLoginUser(httpParser, dos, br);
        }

        ResponseUtils.responseBody(dos, body);
    }

    private static byte[] bindStatics(HttpParser httpParser, DataOutputStream dos) throws IOException, URISyntaxException {
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
