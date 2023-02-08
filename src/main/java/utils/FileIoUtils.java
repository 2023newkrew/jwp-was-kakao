package utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileIoUtils {

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
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            return "";
        }
    }
}
