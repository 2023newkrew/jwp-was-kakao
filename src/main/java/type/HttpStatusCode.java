package type;

public enum HttpStatusCode {
    OK("OK", 200),
    CREATED("CREATED", 201),
    REDIRECT("REDIRECT", 302),
    NOT_FOUND("NOT FOUND", 404),
    ;

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
