package webserver.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RequestBodyTest {

    @DisplayName("RequestBody에 담긴 정보를 추출할 수 있다.")
    @Test
    void findBody() throws IOException {
        // given
        String httpHeader =
            "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 59\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Accept: */*\r\n" +
                "\r\n" +
                "userId=id&password=pw&name=joel&email=joel@joel.jo";

        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpHeader));
        RequestHeader requestHeader = RequestHeader.parse(bufferedReader);
        RequestBody requestBody = RequestBody.parse(bufferedReader, requestHeader);

        // when
        Map<String, String> parsedBody = requestBody.parseRequestBody();

        // then
        assertThat(parsedBody.get("userId")).isEqualTo("id");
        assertThat(parsedBody.get("password")).isEqualTo("pw");
        assertThat(parsedBody.get("name")).isEqualTo("joel");
        assertThat(parsedBody.get("email")).isEqualTo("joel@joel.jo");
    }
}