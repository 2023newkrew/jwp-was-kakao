package application.home;

import application.JinBaseE2ETest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeE2ETest extends JinBaseE2ETest {

    @Test
    void 기본_경로로_요청하면_200과_Hello_world_를_응답한다() {
        // given
        String socketInput = "GET / HTTP/1.1\r\nHost: localhost:8080\r\n\r\n";

        // when
        String socketOutput = getSocketOutputFromWebServer(socketInput);

        // then
        String expectedFirstLine = "HTTP/1.1 200 OK";
        String expectedContentTypeHeader = "Content-Type: application/json;charset=utf-8";
        String expectedContentLengthHeader = "Content-Length: 11";
        String expectedBody = "Hello world";

        assertThat(socketOutput)
                .startsWith(expectedFirstLine)
                .contains(expectedContentTypeHeader)
                .contains(expectedContentLengthHeader)
                .endsWith(expectedBody);
    }
}
