package infra.http.response;

import infra.http.HttpMessageBase;

public class StatusLine {
    public static String DELIMITER = " ";

    private HttpResponseStatus status;
    private String version;

    public StatusLine(HttpResponseStatus status) {
        this.status = status;
        this.version = HttpMessageBase.DEFAULT_VERSION;
    }

    public String toString() {
        return version + DELIMITER + status.code() + DELIMITER + status.value();
    }
}
