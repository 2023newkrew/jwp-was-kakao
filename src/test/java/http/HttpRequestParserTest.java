package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.assertj.core.api.Assertions.*;

class HttpRequestParserTest {

    @DisplayName("HttpRequest 메시지를 파싱하여 HttpRequest 객체를 생성한다.")
    @Test
    void parse() throws IOException {
        // given
        final String httpRequestMessage = String.join("\r\n",
                "GET /css/styles.css HTTP/1.1",
                "Host: localhost:8080",
                "Accept: text/css,*/*;q=0.1",
                "Connection: keep-alive",
                "",
                "");

        final InputStream in = new ByteArrayInputStream(httpRequestMessage.getBytes());

        // when
        HttpRequest parsedRequest = HttpRequestParser.parse(in);

        // then
        HttpRequestLine line = new HttpRequestLine("GET", URI.create("/css/styles.css"), "HTTP/1.1");
        HttpRequestHeader header = new HttpRequestHeader();
        HttpRequestBody body = new HttpRequestBody("");

        header.addAttribute(HttpHeaders.HOST, "localhost:8080");
        header.addAttribute(HttpHeaders.ACCEPT, "text/css,*/*;q=0.1");
        header.addAttribute(HttpHeaders.CONNECTION, "keep-alive");


        HttpRequest request = new HttpRequest(
                line,
                header,
                body
        );

        System.out.println(parsedRequest);

        assertThat(parsedRequest).isEqualTo(request);

    }
}