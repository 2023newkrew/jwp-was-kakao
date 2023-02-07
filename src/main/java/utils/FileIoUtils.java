package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIoUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static boolean isFileExist(String filePath) {
        URL resource = FileIoUtils.class.getClassLoader()
                .getResource(filePath);
        return resource != null;
    }

    public static byte[] loadFileFromClasspath(String filePath) {
        try {
            Path path = Paths.get(FileIoUtils.class.getClassLoader()
                    .getResource(filePath)
                    .toURI());
            return Files.readAllBytes(path);
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage());
            return new byte[0];
        }
    }

    public static String getContentType(String filePath) {
        try {
            Path path = Paths.get(FileIoUtils.class.getClassLoader()
                    .getResource(filePath)
                    .toURI());
            return Files.probeContentType(path);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
            return "";
        }
    }
}
