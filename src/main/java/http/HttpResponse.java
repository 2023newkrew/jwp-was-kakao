package http;

import com.google.common.primitives.Bytes;
import common.Protocol;
import utils.FileIoUtils;

import java.util.Objects;

public class HttpResponse {

    private ResponseInfo responseInfo;
    private Headers headers;
    private Body body;

    public static HttpResponse of(HttpStatus httpStatus, String resourcePath) {
        ResponseInfo responseInfo = new ResponseInfo(Protocol.HTTP1_1, httpStatus);

        Body body = new Body(FileIoUtils.loadFileFromClasspath(resourcePath));

        Headers headers = new Headers();
        headers.put("Content-Type", ContentType.from(resourcePath).toString());
        headers.put("Content-Length", String.valueOf(body.length()));

        return new HttpResponse(responseInfo, headers, body);
    }

    public static HttpResponse of(HttpStatus httpStatus, Body body) {
        ResponseInfo responseInfo = new ResponseInfo(Protocol.HTTP1_1, httpStatus);

        Headers headers = new Headers();
        headers.put("Content-Type", ContentType.TEXT_PLAIN.toString());
        headers.put("Content-Length", String.valueOf(body.length()));

        return new HttpResponse(responseInfo, headers, body);
    }

    private HttpResponse(ResponseInfo responseInfo, Headers headers, Body body) {
        this.responseInfo = responseInfo;
        this.headers = headers;
        this.body = body;
    }

    public byte[] toByte() {
        String responseHeader = String.join(" \r\n", responseInfo.toString(), headers.toString());

        if (Objects.isNull(body)) {
            return responseHeader.getBytes();
        }

        return Bytes.concat((responseHeader + "\r\n".repeat(2)).getBytes(), body.asByte());
    }

}
