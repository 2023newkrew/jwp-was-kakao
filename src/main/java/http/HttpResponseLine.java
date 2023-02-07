package http;

public class HttpResponseLine {
    private final String version;
    private final HttpStatus status;

    public HttpResponseLine(String version, HttpStatus status) {
        this.version = version;
        this.status = status;
    }

    protected byte[] getBytes() {
        String line = "HTTP/" + version + " " + status.getCode() + " " + status.getMessage() + " \r\n";
        return line.getBytes();
    }
}
