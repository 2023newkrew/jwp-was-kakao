package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

public class FileLoadRequestHandleTest {
    @Test
    @DisplayName("/index.html을 잘 불러오는지 테스트")
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then

        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html \r\n" +
                "Content-Length: 7153 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("/user/form.html을 제대로 불러오는지 테스트")
    void userForm() throws URISyntaxException, IOException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/form.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        String[] outputs = socket.output().split("\r\n");

        assertThat(outputs[0]).isEqualTo("HTTP/1.1 200 OK ");
        assertThat(outputs[1]).isEqualTo("Content-Type: text/html;charset=utf-8 ");
        assertThat(outputs[2]).isEqualTo("Content-Length: 5310 ");
    }

}
