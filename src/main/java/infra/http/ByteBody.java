package infra.http;

public class ByteBody implements Body {
    private byte[] value;

    public ByteBody(byte[] value) {
        this.value = value;
    }

    public int length() {
        return value.length;
    }

    public byte[] flat() {
        return value;
    }

    public String toString() {
        return new String(value);
    }
}
