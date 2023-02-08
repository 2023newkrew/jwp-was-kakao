package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpCookieTest {
    @Test
    @DisplayName("Cookie 헤더 문자열을 통한 HttpCookie 생성 테스트")
    void createHttpCookieTest() {
        String cookieParams = "Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/";
        HttpCookie httpCookie = new HttpCookie(cookieParams);

        assertThat(httpCookie.getCookieParam("JSESSIONID")).contains("656cef62-e3c4-40bc-a8df-94732920ed46");
        assertThat(httpCookie.getCookieParam("Path")).contains("/");
    }
}
