package supports;

import db.DataBase;
import model.User;
import utils.FileIoUtils;
import utils.IOUtils;
import utils.ResponseUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;

public class PathBinder {
    private static final String TEMPLATE_ROOT_PATH = "./templates";
    private static final String STATIC_ROOT_PATH = "./static";

    public PathBinder() {

    }

    public void bind(String path, OutputStream out, BufferedReader br, HttpParser httpParser) throws IOException, URISyntaxException {

        byte[] body = new byte[0];
        DataOutputStream dos = new DataOutputStream(out);

        body = bindStatics(path, body, dos);
        body = bindTemplates(path, body, dos);
        bindCreateUser(path, br, httpParser, dos);

        ResponseUtil.responseBody(dos, body);
    }

    private byte[] bindTemplates(String path, byte[] body, DataOutputStream dos) throws IOException, URISyntaxException {
        if (path.endsWith("html") || path.endsWith("html/")) {
            body = FileIoUtils.loadFileFromClasspath(TEMPLATE_ROOT_PATH + path);
            ResponseUtil.response200Header(dos, body.length, path);
        }
        return body;
    }

    private static byte[] bindStatics(String path, byte[] body, DataOutputStream dos) throws IOException, URISyntaxException {
        if (path.startsWith("/css") ||
                path.startsWith("/fonts") ||
                path.startsWith("/images") ||
                path.startsWith("/js")) {
            body = FileIoUtils.loadFileFromClasspath(STATIC_ROOT_PATH + path);
            ResponseUtil.response200Header(dos, body.length, path);
        }
        return body;
    }

    private void bindCreateUser(String path, BufferedReader br, HttpParser httpParser, DataOutputStream dos) throws IOException {
        if (path.startsWith("/user/create")) {
            saveUser(br, httpParser);
            ResponseUtil.response302Header(dos, "/index.html");
        }
    }

    private void saveUser(BufferedReader br, HttpParser httpParser) throws IOException {
        Integer contentLength = httpParser.getContentLength();
        String userBody = IOUtils.readData(br, contentLength);
        HashMap<String, String> queryParam = parseQueryParameter(userBody);

        User user = new User(
                queryParam.get("userId"),
                queryParam.get("password"),
                queryParam.get("name"),
                queryParam.get("email")
        );
        DataBase.addUser(user);
        System.out.println(DataBase.findAll());
    }

    private HashMap<String, String> parseQueryParameter(String userBody) {
        HashMap<String, String> result = new HashMap<>();

        for (String info : userBody.split("&")) {
            String key = info.split("=")[0];
            String value = info.split("=")[1];
            result.put(key, value);
        }

        return result;
    }
}
