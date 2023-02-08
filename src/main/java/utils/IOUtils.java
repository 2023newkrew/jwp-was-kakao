package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
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

    // 쿼리스트링으로부터 key와 value를 추출하여 Map 형태로 반환
    public static Map<String, String> extractParams(String queryString) {
        Map<String, String> resultMap = new HashMap<>();
        String[] datas = queryString.split("&");
        for (String data : datas) {
            String[] strArr = data.split("=");
            resultMap.put(strArr[0], strArr[1]);
        }
        return resultMap;
    }
}
