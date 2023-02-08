package model.user;

import exceptions.InvalidQueryParameterException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtils {
    public static Map<String, String> extractUserFromPath(String path) {
        String[] token = path.split("\\?");
        try {
            return extractUser(token[1]);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidQueryParameterException();
        }
    }

    public static Map<String, String> extractUser(String params) throws IndexOutOfBoundsException {
        String[] queryParams = params.split("&");
        return Arrays.stream(queryParams)
                .map(s -> s.split("="))
                .collect(Collectors.toMap(
                        a -> a[0],
                        a -> URLDecoder.decode(a[1], StandardCharsets.UTF_8)
                ));
    }
}
