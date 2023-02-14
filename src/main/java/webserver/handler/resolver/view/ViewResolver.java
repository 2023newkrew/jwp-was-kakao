package webserver.handler.resolver.view;

import webserver.handler.resolver.AbstractResolver;
import webserver.http.content.ContentType;
import webserver.response.ResponseBody;

public class ViewResolver extends AbstractResolver {

    private final ContentType contentType;

    public ViewResolver(String prefix, ContentType contentType) {
        super(prefix);
        this.contentType = contentType;
    }

    @Override
    public ResponseBody resolve(String path) {
        return new ResponseBody(contentType, getData(path));
    }
}
