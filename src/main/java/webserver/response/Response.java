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
