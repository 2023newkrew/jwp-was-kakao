package utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIoUtils {
    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        Path path = Paths.get(FileIoUtils.class.getClassLoader().getResource(filePath).toURI());
        return Files.readAllBytes(path);
    }

    public static boolean existsFile(String filePath) {

        try {
            URI uri = FileIoUtils.class.getClassLoader().getResource(filePath).toURI();
            File file = new File(uri);
            return file.isFile();
        }
        catch (Exception ignore) {
            return false;
        }
    }
}
