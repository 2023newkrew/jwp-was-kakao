package webserver.resolver;

import utils.FileIoUtils;
import webserver.content.Content;
import webserver.request.Path;

public class ViewResolver implements ContentResolver {

    private static final String PREFIX = "./templates";

    @Override
    public boolean isResolvable(Path path) {
        return path.isRoot()
                || FileIoUtils.existsFile(path.getFullPath(PREFIX));
    }

    @Override
    public Content resolve(Path path) {
        if (path.isRoot()) {
            return resolveRoot();
        }
        try {
            byte[] data = FileIoUtils.loadFileFromClasspath(path.getFullPath(PREFIX));
            return new Content(data);
        }
        catch (Exception ignore) {
            throw new RuntimeException();
        }
    }

    private Content resolveRoot() {
        byte[] data = "Hello world".getBytes();

        return new Content(data);
    }
}
