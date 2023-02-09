package webserver.request;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CookieTest {

    @Test
    void 쿠키문자열을_파싱할_수_있다() {
        Cookie cookie = Cookie.parse("JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/");
        assertThat(cookie.get("JSESSIONID"))
                .isEqualTo("656cef62-e3c4-40bc-a8df-94732920ed46");
        assertThat(cookie.get("Path"))
                .isEqualTo("/");
    }

    @Test
    void 빈_쿠키_문자열에는_아무_값도_저장되지_않는다() {
        Cookie cookie = Cookie.parse("");
        assertThat(cookie.size()).isEqualTo(0);
    }

}
