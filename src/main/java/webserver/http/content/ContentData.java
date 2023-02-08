package webserver.http.content;

import java.util.Objects;

public class ContentData {
    private final byte[] data;

    public ContentData(byte[] data) {
        if (Objects.isNull(data)) {
            data = new byte[0];
        }
        this.data = data;
    }

    public byte[] getBytes() {
        return data.clone();
    }

    public int size() {
        return data.length;
    }

}
