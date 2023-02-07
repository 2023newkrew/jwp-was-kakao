package model.http;

public enum CustomHttpStatus {

    OK(200, "HTTP/1.1 200 OK"),
    FOUND(302, "HTTP/1.1 302 FOUND");

    private int code;
    private String line;

    CustomHttpStatus(int code, String line) {
        this.code = code;
        this.line = line;
    }

    public int getCode() {
        return code;
    }

    public String getLine() {
        return line;
    }

}
