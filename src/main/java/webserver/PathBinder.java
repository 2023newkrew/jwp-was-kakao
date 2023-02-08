package webserver;

import supports.HttpParser;
import supports.UserService;
import utils.FileIoUtils;
import utils.ResponseUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class PathBinder {
    private static final String TEMPLATE_ROOT_PATH = "./templates";
    private static final String STATIC_ROOT_PATH = "./static";
    private static final String HTML = "html";
    private static final String CSS = "/css";
    private static final String FONTS = "/fonts";
    private static final String IMAGES = "/images";
    private static final String JS = "/js";
    private static final String USER_CREATE_URL = "/user/create";
    private static final String INDEX_PATH = "/index.html";

    private final UserService userService;

    public PathBinder() {
        userService = new UserService();
    }

    public void bind(String path, OutputStream out, BufferedReader br, HttpParser httpParser) throws IOException, URISyntaxException {

        byte[] body = new byte[0];
        DataOutputStream dos = new DataOutputStream(out);

        body = bindStatics(path, body, dos);
        body = bindTemplates(path, body, dos);
        bindCreateUser(path, br, httpParser, dos);

        ResponseUtils.responseBody(dos, body);
    }

    private byte[] bindTemplates(String path, byte[] body, DataOutputStream dos) throws IOException, URISyntaxException {
        if (path.endsWith(HTML)) {
            body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
            ResponseUtils.responseOkHeader(dos, body.length, path);
        }
        return body;
    }

    private static byte[] bindStatics(String path, byte[] body, DataOutputStream dos) throws IOException, URISyntaxException {
        if (path.startsWith(CSS) ||
                path.startsWith(FONTS) ||
                path.startsWith(IMAGES) ||
                path.startsWith(JS)) {
            body = FileIoUtils.loadFileFromClasspath(STATIC_ROOT_PATH + path);
            ResponseUtils.responseOkHeader(dos, body.length, path);
        }
        return body;
    }

    private void bindCreateUser(String path, BufferedReader br, HttpParser httpParser, DataOutputStream dos) throws IOException {
        if (path.startsWith(USER_CREATE_URL)) {
            userService.saveUser(br, httpParser);
            ResponseUtils.responseRedirectHeader(dos, INDEX_PATH);
        }
    }
}
