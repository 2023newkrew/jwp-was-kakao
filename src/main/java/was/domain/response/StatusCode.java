package was.domain.response;

public enum StatusCode {
    OK(200, "OK"),
    CREATED(201, "CREATED"),
    NOT_FOUND(404, "NOT_FOUND"),
    FOUND(302, "FOUND");

    private final int code;
    private final String description;

    StatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%d %s", code, description);
    }
}
