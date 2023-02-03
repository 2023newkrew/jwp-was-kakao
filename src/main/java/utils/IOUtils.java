package utils;

import exceptions.InvalidQueryParameterException;
import model.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

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
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String extractRequestFirstLine(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            List<String> headers = new ArrayList<>();
            String line;
            while (!"".equals(line = bufferedReader.readLine())) {
                headers.add(line);
                if (line == null) break;
            }
            System.out.println(headers);
            return headers.get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractPath(String requestFirstLine){
        String[] token = requestFirstLine.split(" ");
        return token[1];
    }

    public static Map<String,String> extractUser(String path) throws IndexOutOfBoundsException{
        String[] token = path.split("\\?");
        try {
            String query = token[1];
            String[] queryParams = query.split("&");
            return Arrays.stream(queryParams).map(s -> s.split("="))
                    .collect(Collectors.toMap(
                            a -> a[0],  //key
                            a -> a[1]   //value
                    ));
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidQueryParameterException();
        }
    }
}
