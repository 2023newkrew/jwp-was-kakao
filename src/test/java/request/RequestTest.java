package request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.model.request.Cookie;
import webserver.model.request.Method;
import webserver.model.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.stream.Stream;

public class RequestTest {

    @ParameterizedTest
    @MethodSource("requestTestSource")
    void method(String url, Request expected) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(url));
        Assertions.assertThat(Request.parse(reader).getMethod()).isEqualTo(expected.getMethod());
    }

    @ParameterizedTest
    @MethodSource("requestTestSource")
    void path(String url, Request expected) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(url));
        Assertions.assertThat(Request.parse(reader).getPath()).isEqualTo(expected.getPath());
    }

    @ParameterizedTest
    @MethodSource("requestTestSource")
    void queryString(String url, Request expected) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(url));
        Assertions.assertThat(Request.parse(reader).getQueryString()).isEqualTo(expected.getQueryString());
    }

    @ParameterizedTest
    @MethodSource("requestTestSource")
    void protocol(String url, Request expected) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(url));
        Assertions.assertThat(Request.parse(reader).getProtocol()).isEqualTo(expected.getProtocol());
    }

    @ParameterizedTest
    @MethodSource("requestTestSource")
    void requestHeader(String url, Request expected) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(url));
        Assertions.assertThat(Request.parse(reader).getRequestHeader()).isEqualTo(expected.getRequestHeader());
    }

    @ParameterizedTest
    @MethodSource("requestTestSource")
    void cookie(String url, Request expected) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(url));
        Assertions.assertThat(Request.parse(reader).getCookie()).isEqualTo(expected.getCookie());
    }

    @ParameterizedTest
    @MethodSource("requestTestSource")
    void requestBody(String url, Request expected) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(url));
        Assertions.assertThat(Request.parse(reader).getRequestBody()).isEqualTo(expected.getRequestBody());
    }


    public static Stream<Arguments> requestTestSource() {
        return Stream.of(
                Arguments.of("GET /index.html HTTP/1.1 \r\n" +
                                "HOST: localhost:8080 \r\n" +
                                "Connection: keep-alive \r\n" +
                                "\r\n" +
                                ""
                        , Request.builder()
                                .method(Method.GET)
                                .path("/index.html")
                                .queryString(Map.of())
                                .protocol("HTTP/1.1")
                                .requestHeader(Map.of("Connection", "keep-alive", "HOST", "localhost"))
                                .cookie(Cookie.empty())
                                .requestBody("")
                                .build()
                ),
                Arguments.of("POST /user/create HTTP/1.1 \r\n" +
                                "HOST: localhost:8080 \r\n" +
                                "Connection: keep-alive \r\n" +
                                "Content-Length: 92 \r\n" +
                                "Content-Type: application/x-www-form-urlencoded \r\n" +
                                "\r\n" +
                                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com"
                        , Request.builder()
                                .method(Method.POST)
                                .path("/user/create")
                                .queryString(Map.of())
                                .protocol("HTTP/1.1")
                                .requestHeader(Map.of("Connection", "keep-alive", "HOST", "localhost", "Content-Length", "92", "Content-Type", "application/x-www-form-urlencoded"))
                                .cookie(Cookie.empty())
                                .requestBody("userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com")
                                .build()
                ),
                Arguments.of("GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1 \r\n" +
                                "HOST: localhost:8080 \r\n" +
                                "Connection: keep-alive \r\n" +
                                "\r\n" +
                                ""
                        , Request.builder()
                                .method(Method.GET)
                                .path("/user/create")
                                .queryString(Map.of("userId", "cu", "password", "password", "name", "%EC%9D%B4%EB%8F%99%EA%B7%9C", "email", "brainbackdoor%40gmail.com"))
                                .protocol("HTTP/1.1")
                                .requestHeader(Map.of("Connection", "keep-alive", "HOST", "localhost"))
                                .cookie(Cookie.empty())
                                .requestBody("")
                                .build()
                )
        );
    }

}
