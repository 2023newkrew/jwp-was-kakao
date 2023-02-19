package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.FileType;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RequestLineTest {

    @DisplayName("RequestLine을 파싱할 수 있다")
    @Test
    void parse() {
        // given & when
        RequestLine requestLine = RequestLine.parse("POST /user/create HTTP/1.1");

        // then
        assertThat(requestLine.getMethod()).isEqualTo(Method.POST);
        assertThat(requestLine.hasQueryString()).isFalse();
        assertThat(requestLine.getQueryString()).isEmpty();
        assertThat(requestLine.findRequestedFileType()).isEqualTo(FileType.HANDLER);
    }

    @DisplayName("RequestLine에 쿼리스트링이 있다면 파싱할 수 있다.")
    @Test
    void parseQueryString() {
        // given
        RequestLine requestLine = RequestLine.parse("GET /user/create?id=id&pw=pw HTTP/1.1");

        // when
        Map<String, String> queryString = requestLine.parseQueryString();

        // then
        assertThat(queryString.get("id")).isEqualTo("id");
        assertThat(queryString.get("pw")).isEqualTo("pw");
    }
}