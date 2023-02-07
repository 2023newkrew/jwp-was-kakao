package app.controller.support;

public enum ResouceType {
    HTML(".html"),
    CSS(".css");

    private final String value;

    ResouceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
