package webserver.http.content;

import java.util.Objects;

public class ContentData {
    private final byte[] data;

    public ContentData(String data) {
        this(data.getBytes());
    }

    public ContentData(byte[] data) {
        if (Objects.isNull(data)) {
            data = new byte[0];
        }
        this.data = data;
    }

    public byte[] getData() {
        return data.clone();
    }

    public int size() {
        return data.length;
    }

    @Override
    public String toString() {
        return new String(data);
    }
}
