package infra.http;

public class StringBody implements Body {
    private String value;

    public StringBody(String value) {
        this.value = value;
    }

    public int length() {
        return value.length();
    }

    public byte[] flat() {
        return value.getBytes();
    }

    public String toString() {
        return value;
    }
}
