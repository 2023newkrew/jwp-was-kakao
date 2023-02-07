package webserver.http;

import http.HttpMethod;
import http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestReaderTest {

    @Nested
    @DisplayName("readHttpRequest 메소드는")
    class ReadHttpRequest {
        @Nested
        @DisplayName("메시지 바디가 없는 HTTP 요청 메시지에 대한 InputStream을 인자로 넘겨받으면")
        class InputStreamWithNoEmptyBody {
            InputStream inputStream;

            @BeforeEach
            void setUp() {
                String request = String.join("\r\n",
                        "GET /index.html HTTP/1.1",
                        "Host: localhost:8080",
                        "Connection: keep-alive",
                        "Accept: text/html, application/xhtml+xml",
                        "",
                        "");

                inputStream = new ByteArrayInputStream(request.getBytes());
            }

            @Test
            @DisplayName("InputStream을 읽어서 body가 빈 문자열인 Request 객체로 파싱해서 반환한다")
            void returnHttpRequest() {
                Map<String, List<String>> headers = new HashMap<>();
                headers.put("Host", List.of("localhost:8080"));
                headers.put("Connection", List.of("keep-alive"));
                headers.put("Accept", List.of("text/html", "application/xhtml+xml"));

                HttpRequest expected = HttpRequest.HttpRequestBuilder.aHttpRequest()
                        .withMethod(HttpMethod.GET)
                        .withURL("/index.html")
                        .withVersion("HTTP/1.1")
                        .withHeaders(headers)
                        .build();

                HttpRequest request;
                try (HttpRequestReader httpRequestReader = new HttpRequestReader(inputStream)) {
                    request = httpRequestReader.readHttpRequest();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                assertThat(request).isEqualTo(expected);
            }
        }

        @Nested
        @DisplayName("메시지 바디가 있는 HTTP 요청 메시지에 대한 InputStream을 인자로 넘겨받으면")
        class InputStreamWithBody {
            InputStream inputStream;
            String body;

            @BeforeEach
            void setUp() {
                body = "name=john&password=1234";

                String request = String.join("\r\n",
                        "POST /user/create HTTP/1.1 ",
                        "Host: localhost:8080 ",
                        "Connection: keep-alive ",
                        "Content-Length: " + body.length(),
                        "",
                        body);

                inputStream = new ByteArrayInputStream(request.getBytes());
            }

            @Test
            @DisplayName("InputStream을 읽어서 body가 빈 문자열인 Request 객체로 파싱해서 반환한다")
            void returnHttpRequest() {
                Map<String, List<String>> headers = new HashMap<>();
                headers.put("Host", List.of("localhost:8080"));
                headers.put("Connection", List.of("keep-alive"));
                headers.put("Content-Length", List.of(String.valueOf(body.length())));

                HttpRequest expected = HttpRequest.HttpRequestBuilder.aHttpRequest()
                        .withMethod(HttpMethod.POST)
                        .withURL("/user/create")
                        .withVersion("HTTP/1.1")
                        .withHeaders(headers)
                        .withBody(body)
                        .build();

                HttpRequest request;
                try (HttpRequestReader httpRequestReader = new HttpRequestReader(inputStream)) {
                    request = httpRequestReader.readHttpRequest();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                assertThat(request).isEqualTo(expected);
            }
        }
    }
}
