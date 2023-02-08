package utils;

import mock.FakeBufferedReader;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.builder.RequestBuilder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class RequestBuilderTest {
    @Test
    @DisplayName("HttpRequest 생성 테스트")
    void IsHttpRequestCreate() throws IOException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        // when
        HttpRequest result = RequestBuilder.getHttpRequest(new FakeBufferedReader());

        // then
        assertThat(result.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(result.getProtocol()).isEqualTo("HTTP/1.1");
        assertThat(result.getURL()).isEqualTo("/index.html");
        assertThat(result.findHeaderValue("Host", null)).isEqualTo("localhost:8080");
        assertThat(result.findHeaderValue("Connection", null)).isEqualTo("keep-alive");
    }
}
