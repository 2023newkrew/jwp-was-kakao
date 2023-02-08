package webserver.response;

import webserver.common.FileType;
import webserver.common.HttpHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Response {

    private final StatusCode statusCode;
    private final Map<HttpHeader, String> responseHeader;
    private final byte[] body;

    private Response(StatusCode statusCode, Map<HttpHeader, String> responseHeader, byte[] body) {
        this.statusCode = statusCode;
        this.responseHeader = responseHeader;
        this.body = body;
    }

    public static Response ok(byte[] body, FileType fileType) {
        Map<HttpHeader, String> responseHeader = generateResponseHeader(body, fileType);
        return new Response(
                StatusCode.OK,
                responseHeader,
                body
        );
    }

    public static Response found(byte[] body, FileType fileType, String location) {
        Map<HttpHeader, String> responseHeader = generateResponseHeader(body, fileType);
        responseHeader.put(HttpHeader.LOCATION, location);
        return new Response(
                StatusCode.FOUND,
                responseHeader,
                body
        );
    }

    private static Map<HttpHeader, String> generateResponseHeader(byte[] body, FileType fileType) {
        Map<HttpHeader, String> responseHeader = new LinkedHashMap<>();
        responseHeader.put(HttpHeader.CONTENT_TYPE, fileType.getContentType() + ";charset=utf-8");
        if (body.length > 0) {
            responseHeader.put(HttpHeader.CONTENT_LENGTH, String.valueOf(body.length));
        }
        return responseHeader;
    }

    public void setHeader(HttpHeader httpHeader, String value) {
        responseHeader.put(httpHeader, value);
    }

    public void flush(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + " \r\n");
        for (Map.Entry<HttpHeader, String> header : responseHeader.entrySet()) {
            dos.writeBytes(header.getKey().getHeaderValue() + ": " + header.getValue() + " \r\n");
        }
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
