package annotation;

public enum Version {
    HTTP_1_1("HTTP/1.1");

    private String name;

    Version(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
