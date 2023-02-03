package webserver;

import org.springframework.http.HttpMethod;
import utils.FileIoUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class HeaderInfo {
    private HttpMethod method;
    private final String path;
    private String root;
    private final Map<String, String> mappings = new HashMap<>();
    private final Map<String, String> queryMappings = new HashMap<>();
    private final Map<String, String> bodyMappings = new HashMap<>();

    private String accept = "text/html";

    public HeaderInfo(String firstLine) {
        if (Objects.isNull(firstLine)) {
            throw new RuntimeException("잘못된 HTTP 메시지입니다. 첫 줄은 \"메소드 경로\" 의 형식이어야 합니다.");
        }
        String[] tokens = firstLine.split(" ");
        HttpMethod method = HttpMethod.resolve(tokens[0]);
        if (Objects.isNull(method)) {
            throw new RuntimeException("잘못된 HTTP 메소드입니다. ");
        }
        this.method = method;
        this.path = setPath(tokens[1]);
    }

    public void readNextLine(String line) {
        String[] tokens = line.split(": ");
        if (tokens[0].equals("Accept")) {
            setAccept(tokens[1]);
            return;
        }
        mappings.put(tokens[0], tokens[1]);
    }

    public byte[] getResponse() {
        try {
            return FileIoUtils.loadFileFromClasspath(root + path);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("잘못된 경로의 요청");
        } catch (NullPointerException e) {
            return new byte[]{};
        }
    }

    public void setBodyParams(String params) {
        params = URLDecoder.decode(params, StandardCharsets.UTF_8);
        for (String parameter : params.split("&")) {
            String[] queryTokens = parameter.split("=");
            String key = queryTokens[0];
            String value = queryTokens[1];
            bodyMappings.put(key, value);
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getRoot() {
        return root;
    }

    public Map<String, String> getMappings() {
        return mappings;
    }

    public Map<String, String> getQueryMappings() {
        return queryMappings;
    }

    public String getHeaderValue(String key) {
        return mappings.get(key);
    }

    public String getQueryValue(String key) {
        return queryMappings.get(key);
    }

    public String getBodyValue(String key) {
        return bodyMappings.get(key);
    }

    public String getAccept() {
        return accept;
    }

    private void setQueryParams(String params) {
        for (String parameter : params.split("&")) {
            String[] queryTokens = parameter.split("=");
            String key = queryTokens[0];
            String value = queryTokens[1];
            queryMappings.put(key, value);
        }
    }

    private void setAccept(String value) {
        //TODO exception handling
        accept = value.split(",")[0];
    }

    private String setPath(String path) {
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        String[] tokens = path.split("\\?");
        if (tokens.length > 2) {
            setQueryParams(tokens[1]);
        }

        String[] pathTokens = tokens[0].split("/");

        if (pathTokens.length < 2) {
            return tokens[0];
        }
        StaticDirectory directory = StaticDirectory.resolve(pathTokens[1].toUpperCase());
        if (Objects.isNull(directory)) {
            root = "./templates";
            return tokens[0];
        }
        root = "./static";

        return tokens[0];
    }
}