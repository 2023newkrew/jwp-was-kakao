package utils;

import error.ApplicationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static error.ErrorType.FILE_READ_FAILED;

public class IOUtils {

    public static byte[] readFileFromClasspath(String filePath) {
        try {
            File file = new File(IOUtils.class.getClassLoader().getResource(filePath).getPath());
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new ApplicationException(FILE_READ_FAILED, e.getMessage());
        }
    }

    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
