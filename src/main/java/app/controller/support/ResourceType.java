package app.controller.support;

public enum ResourceType {
    HTML(".html"),
    CSS(".css");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
