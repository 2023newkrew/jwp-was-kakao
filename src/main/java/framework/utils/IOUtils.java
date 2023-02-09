package framework.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class IOUtils {
    /**
     * @param br 는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength 는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        if (contentLength == 0) return null;

        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static void writeResponse(DataOutputStream dos, byte[] response) throws IOException {
        dos.write(response);
        dos.flush();
    }
}
