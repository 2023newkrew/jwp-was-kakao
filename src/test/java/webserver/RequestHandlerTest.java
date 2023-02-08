package webserver;

import controller.FrontController;
import controller.HomeController;
import controller.StaticController;
import controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    private final FrontController frontController;

    public RequestHandlerTest() {
        this.frontController = new FrontController();
        this.frontController.addAll(new HomeController(), new UserController(), new StaticController());
    }

    @Test
    void socket_out() {
        // given
        final var socket = new StubSocket();
        final var handler = new RequestHandler(socket, frontController);

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
        final RequestHandler handler = new RequestHandler(socket, frontController);

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