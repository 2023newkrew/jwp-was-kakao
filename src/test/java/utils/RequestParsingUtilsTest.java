package utils;

import http.HttpMethod;
import http.HttpRequestHeader;
import http.Uri;
import http.request.RequestHeaders;
import http.request.RequestStartLine;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RequestParsingUtilsTest {
    @Test
    void parseStartLine() {
        String rawStartLine = "GET /index.html HTTP/1.1 ";
        RequestStartLine startLine = RequestParsingUtils.parseStartLine(rawStartLine);

        HttpMethod method = startLine.getMethod();
        Uri uri = startLine.getUri();
        String version = startLine.getVersion();

        assertThat(method.isGet()).isTrue();
        assertThat(uri.getUri()).isEqualTo("/index.html");
        assertThat(version).isEqualTo("HTTP/1.1");
    }

    @Test
    void parseRequestHeader() {
        List<String> rawHeader = List.of(
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*"
        );

        RequestHeaders headers = RequestParsingUtils.parseRequestHeader(rawHeader);


        assertThat(headers.get(HttpRequestHeader.HOST).orElse("")).isEqualTo("localhost:8080");
        assertThat(headers.get(HttpRequestHeader.CONNECTION).orElse("")).isEqualTo("keep-alive");
        assertThat(headers.get(HttpRequestHeader.ACCEPT).orElse("")).isEqualTo("*/*");
    }

    @Test
    void parseQueryString() {
        String query = "username=jay&password=1234&email=abc@def.ghi";

        Map<String, String> params = RequestParsingUtils.parseQueryString(query);

        assertThat(params.getOrDefault("username", "")).isEqualTo("jay");
        assertThat(params.getOrDefault("password", "")).isEqualTo("1234");
        assertThat(params.getOrDefault("email", "")).isEqualTo("abc@def.ghi");
    }
}