package webserver.view;

import webserver.response.MediaType;

public class View {

    private final byte[] content;

    private final MediaType contentType;

    public View(byte[] content, MediaType contentType) {
        if (content == null) {
            content = new byte[0];
        }
        this.content = content;
        this.contentType = contentType;
    }

    public View(byte[] content) {
        this(content, MediaType.TEXT_HTML);
    }

    public String getContent() {
        return new String(content);
    }

    public int getContentLength() {
        return content.length;
    }

    public MediaType getContentType() {
        return contentType;
    }
}
