package webserver;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 6902 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        String output = socket.output();
        int end = Math.min(output.length(), expected.length());
        for(int i=0; i<end; i++) {
            char c1 = output.charAt(i);
            char c2 = expected.charAt(i);
            if(c1 != c2) {
                System.out.println("#### Diff -> "+ i + " : "+c1+" vs "+c2);
                int s =Math.max(0, i-20);
                int e = Math.min(i+20, end);
                String s1 = output.substring(s,e);
                String s2 = expected.substring(s,e);
                System.out.println(s1);
                System.out.println(s2);
                break;
            }
        }
        assertThat(output).isEqualTo(expected);
    }

    @Test
    void createUser() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Connection: keep-alive\n" +
                        "Content-Length: 92\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Accept: */*\n" +
                        "\n" +
                        "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com\n");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        User user = DataBase.findUserById("cu");
        assertThat(user).hasFieldOrPropertyWithValue("password", "password")
                .hasFieldOrPropertyWithValue("name", "이동규")
                .hasFieldOrPropertyWithValue("email", "brainbackdoor@gmail.com");
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: http://localhost:8080/index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }
}