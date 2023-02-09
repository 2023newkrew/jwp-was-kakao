package webserver;

import auth.HttpCookie;
import supports.HttpParser;
import supports.UserService;
import utils.FileIoUtils;
import utils.ResponseUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PathBinder {
    private static final String TEMPLATE_ROOT_PATH = "./templates";
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

    public PathBinder() {
        userService = new UserService();
    }

    public void bind(OutputStream out, BufferedReader br, HttpParser httpParser) throws IOException, URISyntaxException {

        byte[] body = new byte[0];
        DataOutputStream dos = new DataOutputStream(out);

        body = bindStatics(body, httpParser, dos);
        body = bindTemplates(body, httpParser, dos);
        bindCreateUser(br, httpParser, dos);
        bindLoginUser(br, httpParser, dos);

        ResponseUtils.responseBody(dos, body);
    }

    private byte[] bindTemplates(byte[] body, HttpParser httpParser, DataOutputStream dos) throws IOException, URISyntaxException {
        String method = httpParser.getMethod();
        String path = httpParser.getPath();
        if (Objects.equals(method, "GET") && path.endsWith(HTML)) {
            body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
            ResponseUtils.responseOkHeader(dos, body.length, path);
        }
        return body;
    }

    private static byte[] bindStatics(byte[] body, HttpParser httpParser, DataOutputStream dos) throws IOException, URISyntaxException {
        String method = httpParser.getMethod();
        String path = httpParser.getPath();
        if (Objects.equals(method, "GET") &&
                path.startsWith(CSS) ||
                path.startsWith(FONTS) ||
                path.startsWith(IMAGES) ||
                path.startsWith(JS)) {
            body = FileIoUtils.loadFileFromClasspath(STATIC_ROOT_PATH + path);
            ResponseUtils.responseOkHeader(dos, body.length, path);
        }
        return body;
    }

    private void bindCreateUser(BufferedReader br, HttpParser httpParser, DataOutputStream dos) throws IOException {
        String method = httpParser.getMethod();
        String path = httpParser.getPath();
        if (Objects.equals(method, "POST") && path.startsWith(USER_CREATE_URL)) {
            userService.saveUser(br, httpParser);
            ResponseUtils.responseRedirectHeader(dos, INDEX_PATH);
        }
    }

    private void bindLoginUser(BufferedReader br, HttpParser httpParser, DataOutputStream dos) throws IOException {
        String method = httpParser.getMethod();
        String path = httpParser.getPath();
        if (Objects.equals(method, "POST") && path.startsWith(USER_LOGIN_URL)) {
            Optional<UUID> cookie = userService.loginUser(br, httpParser);

            if (cookie.isPresent()){
                HttpCookie httpCookie = new HttpCookie(cookie.get());
                ResponseUtils.responseLoginHeader(dos, httpCookie);
            } else{
                ResponseUtils.responseRedirectHeader(dos, USER_LOGIN_FAIL_PATH);
            }
        }
    }
}
