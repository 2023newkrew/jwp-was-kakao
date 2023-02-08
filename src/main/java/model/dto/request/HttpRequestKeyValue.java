package model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.enumeration.HttpMethod;

@Getter
@AllArgsConstructor
public class HttpRequestKeyValue {
    String key;
    String value;

    public HttpRequestKeyValue(String line) {
        this.key = getKeyWithOutColumn(line);
        this.value = getValue(line);
    }

    private String getValue(String line) {
        return line.split(" ")[1];
    }

    private String getKeyWithOutColumn(String line) {
        return line.split(" ")[0].substring(0, line.split(" ")[0].length() - 1);
    }
}
