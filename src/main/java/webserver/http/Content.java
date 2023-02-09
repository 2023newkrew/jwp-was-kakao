package webserver.http;

public class Content {

    private final ContentType contentType;

    private final ContentData data;

    public Content(ContentType contentType, ContentData data) {
        this.contentType = contentType;
        this.data = data;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return data.size();
    }

    public byte[] getBytes() {
        return data.getBytes();
    }
}
