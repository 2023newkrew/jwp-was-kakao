package webserver.view;

import webserver.response.MediaType;

public class Prefix {

    private final String path;

    private final MediaType contentType;

    public Prefix(String path, MediaType contentType) {
        this.path = path;
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public MediaType getContentType() {
        return contentType;
    }
}
