package http;

public class HttpCookie {
    private String name;
    private String value;
    private String path;

    public HttpCookie(String name, String value) {
        this(name, value, null);
    }

    public HttpCookie(String name, String value, String path) {
        this.name = name;
        this.value = value;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String toHeaderValue() {
        if (name == null || name.isBlank() || value == null || value.isBlank()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(name).append("=").append(value);

        if (path == null || path.isBlank()) {
            return stringBuilder.toString();
        }

        stringBuilder.append("; Path=").append(path);
        return stringBuilder.toString();
    }
}
