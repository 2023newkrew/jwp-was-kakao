package webserver.request;

public class Path {
    private static final String ROOT = "/";

    private final String uri;

    private final PathVariable pathVariable;

    public Path(String path) {
        String[] pathSplit = path.split("\\?");
        this.uri = pathSplit[0];
        if (pathSplit.length < 2) {
            this.pathVariable = null;
            return;
        }
        this.pathVariable = new PathVariable(pathSplit[1]);
    }

    public String getFullPath(String prefix) {
        return prefix + uri;
    }

    public boolean isRoot() {
        return uri.equals(ROOT);
    }

    public boolean isCreate() {
        return uri.equals("/user/create");
    }

    public boolean startWith(String str) {
        return uri.startsWith(str);
    }

    public PathVariable getPathVariable() {
        return pathVariable;
    }
}
