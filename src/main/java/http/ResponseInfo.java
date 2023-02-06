package http;

import common.Protocol;

public class ResponseInfo {

    private Protocol protocol;
    private HttpStatus httpStatus;

    ResponseInfo(Protocol protocol, HttpStatus httpStatus) {
        this.protocol = protocol;
        this.httpStatus = httpStatus;
    }

    public String toString() {
        return String.join(" ", protocol.toString(), String.valueOf(httpStatus.getCode()), httpStatus.getMessage());
    }

}
