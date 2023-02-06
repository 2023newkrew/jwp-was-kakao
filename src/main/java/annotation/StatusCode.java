package annotation;

public enum StatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "NOT_FOUND");

    private int code;
    private String description;

    StatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String toString() {
        return String.format("%d %s", code, description);
    }
}
