package utils;

import support.InvalidParameterException;
import support.MissingParameterException;

import java.util.Map;

public class ValidationUtils {
    public static void checkNotNullAndBlank(Map<String, String> map, String key) {
        if (map.get(key) == null) {
            throw new MissingParameterException(key);
        }
        else if (map.get(key).isBlank()) {
            throw new InvalidParameterException(key);
        }
    }
}
