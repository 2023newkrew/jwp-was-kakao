package webserver.response;

import webserver.http.content.ContentData;
import webserver.http.content.ContentType;

public class ResponseBody {

    private final ContentType contentType;

    private final ContentData data;

    public ResponseBody(ContentType contentType, ContentData data) {
        this.contentType = contentType;
        this.data = data;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return data.size();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
