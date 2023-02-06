package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileIoUtils {

    public static byte[] loadFileFromClasspath(String filePath) {
        try {
            File file = new File(FileIoUtils.class.getClassLoader().getResource(filePath).getPath());
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
