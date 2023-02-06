package http.response;

import com.google.common.primitives.Bytes;
import http.Body;
import http.HttpHeaders;
import http.ContentType;
import http.Protocol;
import utils.IOUtils;

import java.util.Objects;

import static http.HttpHeaders.*;

public class HttpResponse {

    private final ResponseInfo responseInfo;
    private final HttpHeaders httpHeaders;
    private final Body body;

    public static HttpResponse of(HttpStatus httpStatus, String resourcePath) {
        ResponseInfo responseInfo = new ResponseInfo(Protocol.HTTP1_1, httpStatus);
        Body body = new Body(IOUtils.readFileFromClasspath(resourcePath));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(CONTENT_TYPE, ContentType.from(resourcePath).toString());
        httpHeaders.put(CONTENT_LENGTH, String.valueOf(body.length()));

        return new HttpResponse(responseInfo, httpHeaders, body);
    }

    public static HttpResponse of(HttpStatus httpStatus, Body body) {
        ResponseInfo responseInfo = new ResponseInfo(Protocol.HTTP1_1, httpStatus);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
        httpHeaders.put(CONTENT_LENGTH, String.valueOf(body.length()));

        return new HttpResponse(responseInfo, httpHeaders, body);
    }

    public static HttpResponse redirect(String resourcePath) {
        ResponseInfo responseInfo = new ResponseInfo(Protocol.HTTP1_1, HttpStatus.FOUND);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(LOCATION, resourcePath);

        return new HttpResponse(responseInfo, httpHeaders);
    }

    private HttpResponse(ResponseInfo responseInfo, HttpHeaders httpHeaders) {
        this(responseInfo, httpHeaders, null);
    }

    private HttpResponse(ResponseInfo responseInfo, HttpHeaders httpHeaders, Body body) {
        this.responseInfo = responseInfo;
        this.httpHeaders = httpHeaders;
        this.body = body;
    }

    public byte[] toByte() {
        String responseHeader = String.join(" \r\n", responseInfo.toString(), httpHeaders.toString());

        if (Objects.isNull(body)) {
            return responseHeader.getBytes();
        }

        return Bytes.concat((responseHeader + "\r\n".repeat(2)).getBytes(), body.asByte());
    }

}
