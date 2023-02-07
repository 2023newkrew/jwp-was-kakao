package app.controller.support;

public enum UserUri {
    CREATE("/user/create");

    private final String value;

    UserUri(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
