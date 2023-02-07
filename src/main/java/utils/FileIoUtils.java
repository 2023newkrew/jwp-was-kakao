package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIoUtils {
    private static final String[] RESOURCES_TYPE = {"templates", "static"};

    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        try {
            Path path = Paths.get(FileIoUtils.class.getClassLoader()
                    .getResource(filePath)
                    .toURI());
            return Files.readAllBytes(path);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static byte[] getBodyFromPath(String path) throws IOException, URISyntaxException {
        byte[] body;
        for (String resourceType : RESOURCES_TYPE) {
            body = FileIoUtils.loadFileFromClasspath("./" + resourceType + path);
            if (body != null) {
                return body;
            }
        }
        throw new FileNotFoundException();
    }
}
