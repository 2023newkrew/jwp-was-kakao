package was.utils;

import lombok.Getter;

import java.util.Map;

public class PathParamsParser {
    @Getter
    private final String path;
    private final ParamsParser paramsParser;

    public PathParamsParser(String url) {
        String[] result = url.split("\\?",2);
        path = getStringByIndex(result, 0);
        paramsParser = ParamsParser.from(getStringByIndex(result, 1));
    }

    private String getStringByIndex(String[] strings, int index) {
        if (strings.length > index) {
            return strings[index];
        }
        return "";
    }

    public Map<String, String> getParams() {
        return paramsParser.getParams();
    }
}
