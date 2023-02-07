package http.response;

import com.google.common.primitives.Bytes;
import http.Body;
import http.HttpHeaders;
import http.Protocol;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static http.HttpHeaders.*;

public class HttpResponse {

    private final ResponseInfo responseInfo;
    private final HttpHeaders httpHeaders;
    private final Body body;

    private HttpResponse(ResponseInfo responseInfo, HttpHeaders httpHeaders) {
        this(responseInfo, httpHeaders, null);
    }

    private HttpResponse(ResponseInfo responseInfo, HttpHeaders httpHeaders, Body body) {
        this.responseInfo = responseInfo;
        this.httpHeaders = httpHeaders;
        this.body = body;
    }

    public static HttpResponse ok(Supplier<Body> bodySupplier, Function<Body, HttpHeaders> headersFunction) {
        ResponseInfo responseInfo = new ResponseInfo(Protocol.HTTP1_1, HttpStatus.OK);
        Body body = bodySupplier.get();
        HttpHeaders httpHeaders = headersFunction.apply(body);

        return new HttpResponse(responseInfo, httpHeaders, body);
    }

    public static HttpResponse redirect(String resourcePath) {
        ResponseInfo responseInfo = new ResponseInfo(Protocol.HTTP1_1, HttpStatus.FOUND);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(LOCATION, resourcePath);

        return new HttpResponse(responseInfo, httpHeaders);
    }

    public byte[] toByte() {
        String responseHeader = String.join(" \r\n", responseInfo.toString(), httpHeaders.toString());

        if (Objects.isNull(body)) {
            return responseHeader.getBytes();
        }

        return Bytes.concat((responseHeader + "\r\n".repeat(2)).getBytes(), body.asByte());
    }

}
