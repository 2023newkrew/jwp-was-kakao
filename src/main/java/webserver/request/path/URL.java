package webserver.request.path;

public class URL {
    public static final String VARIABLE_DELIMITER = "\\?";

    private final String path;

    private final PathVariables pathVariables;

    public URL(String url) {
        String[] pathSplit = url.split(VARIABLE_DELIMITER);
        this.path = pathSplit[0];
        this.pathVariables = createPathVariable(pathSplit);
    }

    private PathVariables createPathVariable(String[] pathSplit) {
        if (pathSplit.length < 2) {
            return null;
        }
        return new PathVariables(pathSplit[1]);
    }

    public String getPath() {
        return path;
    }

    public String getPathVariable(String key) {
        return pathVariables.get(key);
    }
}
