package webserver.content;

public class Content {

    private final ContentType contentType;

    private final byte[] data;

    public Content(ContentType contentType, byte[] data) {
        if (data == null) {
            data = new byte[0];
        }
        this.data = data;
        this.contentType = contentType;
    }

    public Content(byte[] content) {
        this(ContentType.TEXT_HTML, content);
    }


    @Override
    public String toString() {
        return new String(data);
    }

    public int getContentLength() {
        return data.length;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
