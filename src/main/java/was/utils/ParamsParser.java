package was.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ParamsParser {
    private final Map<String, String> params;

    private ParamsParser(String paramsString) {
        params = getParams(paramsString);
    }

    private Map<String, String> getParams(String result) {
        return Arrays.stream(result.split("&"))
                .map(it -> it.split("=", 2))
                .filter(it -> it.length == 2)
                .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
    }

    public static ParamsParser from(String paramsString) {
        return new ParamsParser(paramsString);
    }
}
