package model.web;

public class Cookie {
    private final String COOKIE_PATH_DELIMITER = "; ";
    private final String key;
    private final String value;

    public Cookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value.split(COOKIE_PATH_DELIMITER)[0];
    }
    public String getPath() {
        return value.split(COOKIE_PATH_DELIMITER)[1];
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
