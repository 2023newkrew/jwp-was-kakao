package utils;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {

    public static String getHttpRequest(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null && !"".equals(line)) {
            sb.append(line).append('\n');
            line = br.readLine();
        }
        return sb.toString();
    }

    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
