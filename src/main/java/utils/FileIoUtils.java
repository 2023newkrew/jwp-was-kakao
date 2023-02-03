package utils;

import enums.ContentType;
import exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIoUtils {
    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        Path path = null;
        try {
            path = Paths.get(FileIoUtils.class.getClassLoader().getResource(filePath).toURI());
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException("요청하신 경로에 파일이 존재하지 않습니다.");
        }
        return Files.readAllBytes(path);
    }

    public static String getResourcePath(String path, ContentType contentType) {
        if (isNotStaticFile(contentType)) {
            return "./templates" + path;
        }
        return "./static" + path;
    }

    private static boolean isNotStaticFile(ContentType contentType) {
        return contentType == ContentType.HTML || contentType == ContentType.NONE;
    }
}
