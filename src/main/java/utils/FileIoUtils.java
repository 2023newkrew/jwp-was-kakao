package utils;

import enums.ContentType;
import exceptions.ResourceNotFoundException;
import http.HttpRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileIoUtils {
    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        Path path;
        try {
            path = Paths.get(FileIoUtils.class.getClassLoader()
                    .getResource(filePath)
                    .toURI());
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException();
        }
        return Files.readAllBytes(path);
    }

    public static String getResourcePath(String path, ContentType contentType) {
        if (Boolean.TRUE.equals(isTemplatesFile(contentType))) {
            return "./templates" + path;
        }
        return "./static" + path;
    }

    public static Boolean isStaticFile(HttpRequest request) {
        ContentType contentType = ContentType.fromFilename(request.getRequestPath());
        return contentType != ContentType.NONE;
    }

    private static Boolean isTemplatesFile(ContentType contentType) {
        return contentType == ContentType.HTML || contentType == ContentType.ICON;
    }
}
