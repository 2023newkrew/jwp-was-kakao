package webserver.handler.resource.resolver;

import utils.FileIoUtils;
import webserver.http.content.ContentData;

public abstract class AbstractResolver implements Resolver {

    private final String prefix;

    protected AbstractResolver(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean isResolvable(String path) {
        return FileIoUtils.existsFile(getFullPath(path));
    }

    protected String getFullPath(String path) {
        return prefix + path;
    }

    protected ContentData getData(String path) {
        try {
            return new ContentData(FileIoUtils.loadFileFromClasspath(getFullPath(path)));
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
