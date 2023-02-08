package webserver;

import java.util.Map;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import webserver.request.HttpMethod;
import webserver.request.StartLine;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestParserTest {

    @Test
    void parseStartLine() {
        String startLine = "GET /index.html HTTP/1.1";

        StartLine actual = RequestParser.extractStartLine(startLine);

        assertThat(actual.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(actual.getUrl()).isEqualTo("/index.html");
        assertThat(actual.getQueryParams()).hasSize(0);
        assertThat(actual.getHttpVersion()).isEqualTo("HTTP/1.1");
    }
    @Test
    void parseHeaders() {
        List<String> request = new ArrayList<>() {{
            add("Host: localhost:8080");
            add("Connection: keep-alive");
            add("Accept: */*");
        }};

        Map<String, String> headers = RequestParser.extractHeader(request);

        assertThat(headers.get("Host")).isEqualTo("localhost:8080");
        assertThat(headers.get("Connection")).isEqualTo("keep-alive");
        assertThat(headers.get("Accept")).isEqualTo("*/*");
    }

    @Test
    void parseStartLine_queryParams() {

        String startLine = "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1";

        StartLine actual = RequestParser.extractStartLine(startLine);

        assertThat(actual.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(actual.getUrl()).isEqualTo("/user/create");
        assertThat(actual.getQueryParams()).containsKey("userId").containsValue("cu");
        assertThat(actual.getQueryParams()).containsKey("password").containsValue("password");
        assertThat(actual.getQueryParams()).containsKey("name").containsValue("%EC%9D%B4%EB%8F%99%EA%B7%9C");
        assertThat(actual.getQueryParams()).containsKey("email").containsValue("brainbackdoor%40gmail.com");
        assertThat(actual.getHttpVersion()).isEqualTo("HTTP/1.1");
    }
}