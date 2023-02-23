package webserver.response;

public enum HttpResponseStatus {
    OK("200 OK"),
    REDIRECT("302 Found"),
    BAD_REQUEST("400 Bad Request"),
    ;

    private final String status;

    HttpResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
