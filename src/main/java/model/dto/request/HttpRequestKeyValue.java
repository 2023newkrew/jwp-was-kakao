package model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpRequestKeyValue {
    String key;
    String value;

    public HttpRequestKeyValue(String line) {
        this.key = getKey(line);
        this.value = getValue(line);
    }

    private String getValue(String line) {
        return line.split(" ")[1];
    }

    private String getKey(String line) {
        return line.split(" ")[0].substring(0, line.split(" ")[0].length() - 1);
    }
}
