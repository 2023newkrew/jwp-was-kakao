package app.controller.support;

public enum Uri {
    ROOT("/index.html"),
    CSS("/css"),
    USER("/user");

    private final String value;

    Uri(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public int length() {
        return value.length();
    }
}
