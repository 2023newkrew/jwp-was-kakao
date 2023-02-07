package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestHeaderTest {
    @Test
    @DisplayName("header로 부터 요청 경로를 얻는다.")
    void extractRequestPathTest() {
        HttpRequestHeader header = new HttpRequestHeader(List.of("GET /index.html HTTP/1.1 \""));
        String expected = "/index.html";
        assertThat(header.getRequestPath()).isEqualTo(expected);
    }

    @Test
    @DisplayName("header로 부터 HTTP 메서드 이름을 얻는다.")
    void extractRequestMethod() {
        HttpRequestHeader header = new HttpRequestHeader(List.of("GET /index.html HTTP/1.1 \""));
        String expected = "GET";
        assertThat(header.getRequestMethod()).isEqualTo(expected);
    }
}
