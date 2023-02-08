package utils;

import request.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static utils.ContentType.*;
import static utils.ContentType.ICO;

public class IOUtils {
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String requestGetUrl(RequestParams request, String s, BufferedReader br) throws IOException {
        if (request.getContentType() == CSS || request.getContentType() == JS ||
                request.getContentType() == WOFF || request.getContentType() == TTF){
            return "./static" + request.getUrl();
        }
        if (request.getContentType() == HTML || request.getContentType() == ICO){
            return "./templates" + request.getUrl();
        }
        while (!"".equals(s)) {
            s = br.readLine();
        }
        return null;
    }

    public static Optional<String> requestPostBody(String requestInfo, StringParser stringParser, BufferedReader br) throws IOException {
        while (stringParser.getContentLength(requestInfo) == -1) {
            requestInfo = br.readLine();
        }
        if (requestInfo.length() == 0) {
            return Optional.empty();
        }
        return Optional.of(IOUtils.readData(br, stringParser.getContentLength(requestInfo)));
    }

    public static String getIndexUrl() {
        return "/index.html";
    }
}
