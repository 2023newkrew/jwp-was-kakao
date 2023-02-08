package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import app.config.AppConfig;
import infra.utils.FileIoUtils;
import infra.webserver.RequestHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import support.StubSocket;

class RequestHandlerTest {
    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket, AppConfig.getInstance().getControllerConfig().getFrontController());

        // when
        handler.run();

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 11 ",
                "",
                "Hello world");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Accept: text/html ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final var handler = new RequestHandler(socket, AppConfig.getInstance().getControllerConfig().getFrontController());

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 6902 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        var output = socket.output().split("\n");
        var expectedOutput = expected.split("\n");

        for(int i=0; i<output.length; i++){
            if(output[i].equals(expectedOutput[i])){
                continue;
            }
            System.out.println("actual: "+output[i]);
            System.out.println("expected: "+ expectedOutput[i]);
        }


        assertThat(socket.output()).isEqualTo(expected);
    }
}