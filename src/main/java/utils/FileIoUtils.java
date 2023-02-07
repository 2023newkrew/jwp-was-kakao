package utils;

import request.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static utils.ContentType.*;

public class FileIoUtils {
    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        System.out.println(filePath);
        Path path = Paths.get(Objects.requireNonNull(FileIoUtils.class.getClassLoader().getResource(filePath)).toURI());
        return Files.readAllBytes(path);
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

    public static byte[] readFile(String requestUrl) throws IOException, URISyntaxException {
        if (requestUrl == null) {
            throw new NullPointerException("Not Exists Url");
        }
        if (requestUrl.equals(getHomeUrl())){
            return "Hello world".getBytes();
        }
        return loadFileFromClasspath(requestUrl);
    }

    public static String requestPostBody(String requestInfo, StringParser stringParser, BufferedReader br) throws IOException {
        while (stringParser.getContentLength(requestInfo) == -1) {
            requestInfo = br.readLine();
        }
        if (requestInfo.length() == 0) {
            return null;
        }
        return IOUtils.readData(br, stringParser.getContentLength(requestInfo));
    }

    public static String getHomeUrl() {
        return "./templates/index.html";
    }
}
