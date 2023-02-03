package webserver.view;

import utils.FileIoUtils;
import webserver.response.MediaType;

import java.util.List;
import java.util.Objects;

public class ViewResolver {

    private final List<Prefix> prefixes;

    public ViewResolver(List<Prefix> prefixes) {
        this.prefixes = prefixes;
    }

    public View resolveByPath(String path) {
        if (isRoot(path)) {
            return resolveRoot();
        }
        try {
            Prefix matchingPrefix = getMatchingPrefix(path);
            byte[] content = FileIoUtils.loadFileFromClasspath(matchingPrefix.getPath() + path);
            return new View(content, matchingPrefix.getContentType());
        }
        catch (Exception ignore) {
            throw new ViewResolverException();
        }
    }

    private boolean isRoot(String path) {
        return Objects.equals(path, "/");
    }

    private View resolveRoot() {
        byte[] content = "Hello world".getBytes();

        return new View(content, MediaType.TEXT_HTML);
    }

    private Prefix getMatchingPrefix(String path) {
        return prefixes.stream()
                .filter(prefix -> FileIoUtils.existsFile(prefix.getPath() + path))
                .findAny()
                .orElseThrow(ViewResolverException::new);
    }
}
