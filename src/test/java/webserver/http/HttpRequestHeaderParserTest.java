package webserver.http;

import http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestHeaderParserTest {

    @Test
    @DisplayName("\r\n 으로 구분된 HTTP 헤더를 파싱해서 Map<String, List<String>> 형태로 반환한다.")
    void parseHeaderString() {
        String headerString =
                "Header1: value1\r\n" +
                        "Header2: value2,value3\r\n" +
                        "Header-3: ";

        HttpRequestHeaderParser httpRequestHeaderParser = new HttpRequestHeaderParser();
        HttpHeaders headers = httpRequestHeaderParser.parse(headerString);

        assertThat(headers.getHeaders())
                .hasSize(3)
                .containsEntry("Header1", List.of("value1"))
                .containsEntry("Header2", List.of("value2", "value3"))
                .containsEntry("Header-3", Collections.emptyList());
    }
}