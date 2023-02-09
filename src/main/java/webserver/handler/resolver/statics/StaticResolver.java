package webserver.handler.resolver.statics;

import webserver.handler.resolver.AbstractResolver;
import webserver.http.content.ContentType;
import webserver.response.ResponseBody;

public class StaticResolver extends AbstractResolver {

    private final StaticTypes staticTypes;

    private final ContentType defaultType;

    public StaticResolver(String prefix, StaticTypes staticTypes, ContentType defaultType) {
        super(prefix);
        this.staticTypes = staticTypes;
        this.defaultType = defaultType;
    }

    @Override
    public ResponseBody resolve(String path) {
        ContentType contentType = staticTypes.getContentType(path, defaultType);

        return new ResponseBody(contentType, getData(path));
    }
}
