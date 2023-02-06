package webserver;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestParserTest {

    @Test
    void parse1() {
        List<String> request = new ArrayList<>() {{
            add("GET /index.html HTTP/1.1");
            add("Host: localhost:8080");
            add("Connection: keep-alive");
            add("Accept: */*");
            add("");
        }};

        Request actual = RequestParser.parse(request);

        assertThat(actual.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(actual.getUrl()).isEqualTo("/index.html");
        assertThat(actual.getQueryParams()).hasSize(0);
        assertThat(actual.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(actual.getHeaders().get("Host")).isEqualTo("localhost:8080");
        assertThat(actual.getHeaders().get("Connection")).isEqualTo("keep-alive");
        assertThat(actual.getHeaders().get("Accept")).isEqualTo("*/*");
        assertThat(actual.getBody()).isNull();
    }

    @Test
    void parse2() {
        List<String> request = new ArrayList<>() {{
            add("GET /index.html HTTP/1.1");
            add("Host: localhost:8080");
            add("Connection: keep-alive");
            add("Accept: */*");
            add("");
            add("body1");
            add("body2");
        }};

        Request actual = RequestParser.parse(request);

        assertThat(actual.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(actual.getUrl()).isEqualTo("/index.html");
        assertThat(actual.getQueryParams()).hasSize(0);
        assertThat(actual.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(actual.getHeaders().get("Host")).isEqualTo("localhost:8080");
        assertThat(actual.getHeaders().get("Connection")).isEqualTo("keep-alive");
        assertThat(actual.getHeaders().get("Accept")).isEqualTo("*/*");
        assertThat(actual.getBody()).isEqualTo("body1\nbody2");
    }

    @Test
    void parse_queryParams() {
        List<String> request = new ArrayList<>() {{
            add("GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1");
            add("Host: localhost:8080");
            add("Connection: keep-alive");
            add("Accept: */*");
            add("");
        }};

        Request actual = RequestParser.parse(request);

        assertThat(actual.getHttpMethod()).isEqualTo(HttpMethod.GET);
        assertThat(actual.getUrl()).isEqualTo("/user/create");
        assertThat(actual.getQueryParams()).containsKey("userId").containsValue("cu");
        assertThat(actual.getQueryParams()).containsKey("password").containsValue("password");
        assertThat(actual.getQueryParams()).containsKey("name").containsValue("%EC%9D%B4%EB%8F%99%EA%B7%9C");
        assertThat(actual.getQueryParams()).containsKey("email").containsValue("brainbackdoor%40gmail.com");
        assertThat(actual.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(actual.getHeaders().get("Host")).isEqualTo("localhost:8080");
        assertThat(actual.getHeaders().get("Connection")).isEqualTo("keep-alive");
        assertThat(actual.getHeaders().get("Accept")).isEqualTo("*/*");
        assertThat(actual.getBody()).isNull();
    }


}