package webserver.response;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import webserver.FileType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {
    // Response Information
    private final StatusCode statusCode;
    private final Map<String, String> responseHeader;
    private final byte[] body;

    // Constant
    private static final String LOCATION = "Location";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CHARSET_UTF8 = "charset=utf-8";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String HEADER_SEPARATOR = ":";
    private static final String NEW_LINE = "\r\n";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String PATH = "path";

    // Method
    public static Response ok(byte[] body, FileType fileType) {
        Map<String, String> responseHeader = generateResponseHeader(body, fileType);
        return new Response(
                StatusCode.OK,
                responseHeader,
                body
        );
    }

    public static Response found(byte[] body, FileType fileType, String location) {
        Map<String, String> responseHeader = generateResponseHeader(body, fileType);
        responseHeader.put(LOCATION, location);
        return new Response(
                StatusCode.FOUND,
                responseHeader,
                body
        );
    }

    public static Response notFound() {
        byte[] body = "404 NOT FOUND".getBytes();
        Map<String, String> responseHeader = generateResponseHeader(body, FileType.HTML);
        return new Response(
                StatusCode.NOT_FOUND,
                responseHeader,
                body
        );
    }

    public void setCookie(String name, String path) {
        responseHeader.put(SET_COOKIE, JSESSIONID + "=" + name + "; " + PATH + "=" + path);
    }

    private static Map<String, String> generateResponseHeader(byte[] body, FileType fileType) {
        Map<String, String> responseHeader = new LinkedHashMap<>();
        responseHeader.put(CONTENT_TYPE, fileType.getContentType() + ";" + CHARSET_UTF8);
        if (body.length > 0) {
            responseHeader.put(CONTENT_LENGTH, String.valueOf(body.length));
        }
        return responseHeader;
    }

    public void flush(DataOutputStream dos) throws IOException {
        dos.writeBytes(HTTP_VERSION + " " + statusCode.getCode() + " " + statusCode.getMessage() + " " + NEW_LINE);
        for (Map.Entry<String, String> entry : responseHeader.entrySet()) {
            dos.writeBytes(entry.getKey() + HEADER_SEPARATOR + " " + entry.getValue() + " " + NEW_LINE);
        }
        dos.writeBytes(NEW_LINE);
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
