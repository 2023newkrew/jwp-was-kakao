package utils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ParameterUtils {

    private static final String PARAMETER_DELIMITER = "&";
    private static final String KEY_VALUE_DELIMITER = "=";

    public static Map<String, String> parse(String params) {
        return Arrays.stream(params.split(PARAMETER_DELIMITER))
                .map(param -> param.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
    }

}
