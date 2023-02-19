package type;

public enum HttpStatusCode {
    OK("OK", 200),
    CREATED("CREATED", 201),
    REDIRECT("REDIRECT", 302),
    BAD_REQUEST("BAD REQUEST", 400),
    NOT_FOUND("NOT FOUND", 404),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR", 500);

    private final String description;
    private final Integer code;

    HttpStatusCode(String description, Integer code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCode() {
        return code;
    }
}
