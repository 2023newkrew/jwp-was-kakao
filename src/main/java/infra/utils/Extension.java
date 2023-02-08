package infra.utils;

public class Extension {

    private final String extension;

    public Extension(String extension) {
        this.extension = extension;
    }

    public boolean isStatic() {
        return !extension.equals("html") && !extension.equals("ico");
    }
}
