package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import was.annotation.RequestMethod;
import was.utils.RequestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class RequestUtilsTest {

    @DisplayName("요청으로 받는 http요청 메시지를 Request 객체로 반환한다")
    @Test
    void getRequestTest() {
        String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        InputStream in = new ByteArrayInputStream(httpRequest.getBytes());

        was.domain.request.Request request = RequestUtils.getRequest(in);

        Assertions.assertThat(request.getMethod()).isEqualTo(RequestMethod.GET);
        Assertions.assertThat(request.getPath()).isEqualTo("/index.html");
        Assertions.assertThat(request.getBody()).isEqualTo("");
    }
}
