package webserver.view;

import utils.FileIoUtils;

import java.util.Objects;

public class ViewResolver {
    private final String prefix;

    public ViewResolver(String prefix) {
        this.prefix = prefix;
    }

    public byte[] resolveByPath(String path) {
        if (isRoot(path)) {
            return resolveRoot();
        }
        try {
            System.out.println("length : " + FileIoUtils.loadFileFromClasspath(prefix + path).length);
            return FileIoUtils.loadFileFromClasspath(prefix + path);
        }
        catch (Exception ignore) {
            throw new ViewResolverException();
        }
    }

    private boolean isRoot(String path) {
        return Objects.equals(path, "/");
    }

    private byte[] resolveRoot() {
        return "Hello world".getBytes();
    }
}
