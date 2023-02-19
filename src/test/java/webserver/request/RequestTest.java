package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.FileType;
import webserver.common.HttpSession;
import webserver.common.HttpSessions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RequestTest {

    @DisplayName("HttpRequest를 기반으로 Request 객체를 생성할 수 있다")
    @Test
    void parse() throws IOException {
        // given
        String httpRequest = "POST /user/create?hello=world HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Content-Length: 59\r\n" +
            "Content-Type: application/x-www-form-urlencoded\r\n" +
            "Accept: */*\r\n" +
            "\r\n" +
            "id=id&pw=pw&name=name";

        // when
        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpRequest));
        Request request = Request.parse(bufferedReader);

        // then
        assertThat(request.getMethod()).isEqualTo(Method.POST);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.findRequestedFileType()).isEqualTo(FileType.HANDLER);
        assertThat(request.getQueryString()).isEqualTo(Map.of("hello", "world"));
        assertThat(request.getRequestBody()).isEqualTo(
            Map.of(
                "id", "id",
                "pw", "pw",
                "name", "name"
            )
        );
    }

    @DisplayName("HttpRequest를 기반으로 유저의 HttpSession에 접근할 수 있다.")
    @Test
    void accessSession() throws IOException {
        // given
        HttpSession userSession = HttpSessions.create();
        String userSessionId = userSession.getId();

        String httpRequest = "POST /user/create HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Content-Length: 59\r\n" +
            "Content-Type: application/x-www-form-urlencoded\r\n" +
            "Accept: */*\r\n" +
            "Cookie: JSESSIONID=" + userSessionId + "; Path=/\r\n" +
            "\r\n" +
            "userId=id&password=pw&name=name";

        // when
        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpRequest));
        Request request = Request.parse(bufferedReader);

        // then
        assertThat(request.getMethod()).isEqualTo(Method.POST);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.findRequestedFileType()).isEqualTo(FileType.HANDLER);
        assertThat(request.getQueryString()).isEmpty();
        assertThat(request.getSessionId()).isEqualTo(userSessionId);
        assertThat(request.getSession()).isEqualTo(userSession);
    }
}
