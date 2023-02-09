package webserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestLineParserTest {

    HttpRequestLineParser httpRequestLineParser;

    @BeforeEach
    void setUp() {
        httpRequestLineParser = new HttpRequestLineParser();
    }

    @Nested
    @DisplayName("extractHttpMethod 메소드는")
    class ExtractHttpMethod {
        @Nested
        @DisplayName("공백으로 구분된 3개의 토큰으로 구성된 문자열이 주어지면")
        class CorrectRequestLine {
            String requestLine = "GET /test/sample.html?paramA=1&paramB=test HTTP/1.1";

            @Test
            @DisplayName("첫 번째 토큰을 추출해서 반환한다")
            void returnHttpMethod() {
                assertThat(httpRequestLineParser.extractHttpMethod(requestLine))
                        .isEqualTo("GET");
            }
        }
    }

    @Nested
    @DisplayName("extractUrl 메소드는")
    class ExtractUrl {
        @Nested
        @DisplayName("쿼리 파라미터가 있는 url이 포함된 request line이 주어지면")
        class UrlWithParams {
            String requestLine = "GET /test/sample.html?paramA=1&paramB=test HTTP/1.1";

            @Test
            @DisplayName("쿼리 파라미터를 제외한 url을 반환한다")
            void returnHttpMethod() {
                assertThat(httpRequestLineParser.extractUrl(requestLine))
                        .isEqualTo("/test/sample.html");
            }
        }
    }

    @Nested
    @DisplayName("extractParams 메소드는")
    class ExtractParams {
        @Nested
        @DisplayName("쿼리 파라미터가 있는 url이 포함된 request line이 주어지면")
        class UrlWithParams {
            String requestLine = "GET /test/sample.html?a=1&b&c=1=one&& HTTP/1.1";

            @Test
            @DisplayName("쿼리 파라미터를 추출해서 Map 형태로 반환한다")
            void returnParamMap() {
                assertThat(httpRequestLineParser.extractParams(requestLine).getParameters())
                        .hasSize(3)
                        .containsEntry("a", "1")
                        .containsEntry("b", "")
                        .containsEntry("c", "1=one");
            }
        }
    }

    @Nested
    @DisplayName("extractHttpVersion 메소드는")
    class ExtractHttpVersion {
        @Nested
        @DisplayName("공백으로 구분된 3개의 토큰으로 구성된 문자열이 주어지면")
        class CorrectRequestLine {
            String requestLine = "GET /test/sample.html?paramA=1&paramB=test HTTP/1.1";

            @Test
            @DisplayName("세 번째 토큰을 추출해서 반환한다")
            void returnHttpVersion() {
                assertThat(httpRequestLineParser.extractHttpVersion(requestLine))
                        .isEqualTo("HTTP/1.1");
            }
        }
    }
}