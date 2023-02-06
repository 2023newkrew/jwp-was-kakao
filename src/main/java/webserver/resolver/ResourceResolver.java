package webserver.resolver;

import utils.FileIoUtils;
import webserver.content.Content;
import webserver.request.Path;

public class ResourceResolver implements ContentResolver {

    private static final String PREFIX = "./static";

    @Override
    public boolean isResolvable(Path path) {
        return FileIoUtils.existsFile(path.getFullPath(PREFIX));
    }

    @Override
    public Content resolve(Path path) {
        if (!isResolvable(path)) {
            throw new RuntimeException();
        }
        var resourceType = ResourceType.from(path);

        return new Content(resourceType.getContentType(), getResource(path));
    }

    private byte[] getResource(Path path) {
        try {
            return FileIoUtils.loadFileFromClasspath(path.getFullPath(PREFIX));
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
