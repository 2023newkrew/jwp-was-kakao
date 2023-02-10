package view;

import org.springframework.util.AntPathMatcher;
import utils.FileIoUtils;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Arrays;

public class StaticViewRenderer implements ViewRenderer {
    private static final String[] STATIC_PATH_PATTERNS = {"/css/*", "/fonts/*", "/images/*", "/js/*"};
    private static final String STATIC_PATH_PREFIX = "./static";

    private final String url;
    private final String contentType;

    public StaticViewRenderer(String url) {
        this.url = url;
        this.contentType = resolveContentType();
    }

    @Override
    public void render(HttpResponse httpResponse) throws IOException {
        render(httpResponse, null);
    }

    @Override
    public void render(HttpResponse httpResponse, Object context) throws IOException {
        byte[] body = FileIoUtils.loadFileFromClasspath(STATIC_PATH_PREFIX + url);
        httpResponse.addHeader("Content-Type", contentType + ";charset=utf-8");
        httpResponse.changeBody(body);
    }

    public static boolean isStaticUrl(String url) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return Arrays.stream(STATIC_PATH_PATTERNS)
                .anyMatch(pattern -> antPathMatcher.match(pattern, url));
    }

    private static String resolveContentType() {
        // TODO
        return "text/css";
    }
}
