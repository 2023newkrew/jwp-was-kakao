package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import java.io.IOException;
import java.net.URISyntaxException;
import model.User;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;

class RequestHandlerTest {

    private final TemplateLoader loader = new ClassPathTemplateLoader();

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Cookie: JSESSIONID=test",
                "",
                "");

        SessionManager.add("test", new Session("test"));
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 7153 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
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
                        "Cookie: JSESSIONID=test\n" +
                        "\n" +
                        "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com\n");

        SessionManager.add("test", new Session("test"));
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
                "Location: /index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    void login() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1 \r\n" +
                        "Host: localhost:8080 \r\n" +
                        "Connection: keep-alive\n" +
                        "Content-Length: 27\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Accept: */*\n" +
                        "Cookie: JSESSIONID=test\n" +
                        "\r\n" +
                        "userId=cu&password=password \r\n");

        SessionManager.add("test", new Session("test"));
        DataBase.addUser(new User("cu", "password", "이동규", "brainbackdoor%40gmail.com"));
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: /index.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
        assertThat(SessionManager.findSession("test").getAttributes("user")).isEqualTo(DataBase.findUserById("cu"));
    }

    @Test
    void loginFailed() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "POST /user/login HTTP/1.1 \r\n" +
                        "Host: localhost:8080 \r\n" +
                        "Connection: keep-alive\n" +
                        "Content-Length: 27\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Accept: */*\n" +
                        "Cookie: JSESSIONID=test\n" +
                        "\r\n" +
                        "userId=cu&password=1234 \r\n");

        SessionManager.add("test", new Session("test"));
        DataBase.addUser(new User("cu", "password", "이동규", "brainbackdoor%40gmail.com"));
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        var expected = "HTTP/1.1 302 FOUND \r\n" +
                "Location: /user/login_failed.html \r\n" +
                "\r\n";

        assertThat(socket.output()).isEqualTo(expected);
        assertThat(SessionManager.findSession("test").getAttributes("user")).isNull();
    }

    @Test
    void showUserList() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /user/list HTTP/1.1 \r\n" +
                        "Host: localhost:8080 \r\n" +
                        "Connection: keep-alive\n" +
                        "Cookie: JSESSIONID=test\n" +
                        "\r\n");
        Session session = new Session("test");
        SessionManager.add("test", session);
        User currentUser = new User("cu", "password", "test1", "brainbackdoor1%40gmail.com");
        DataBase.addUser(currentUser);
        session.setAttribute("user", currentUser);
        DataBase.addUser(new User("cu2", "password", "test2", "brainbackdoor2%40gmail.com"));
        DataBase.addUser(new User("cu3", "password", "test3", "brainbackdoor3%40gmail.com"));
        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket);

        // when
        handler.run();

        // then
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);
        Template template = handlebars.compile("/user/list");
        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 5553 \r\n" +
                "\r\n" +
                template.apply(DataBase.findAll());

        assertThat(socket.output()).isEqualTo(expected);
    }
}