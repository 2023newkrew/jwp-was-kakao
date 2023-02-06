package utils;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param BufferedReader
     *            Request Body를 시작하는 시점이어야
     * @param contentLength
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readHeader(BufferedReader br) throws IOException{
        StringBuilder httpHeaderString = new StringBuilder();
        String line = br.readLine();
        while (!"".equals(line) && line != null) {
            httpHeaderString.append(line).append('\n');
            line = br.readLine();
        }

        return httpHeaderString.toString();
    }
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
