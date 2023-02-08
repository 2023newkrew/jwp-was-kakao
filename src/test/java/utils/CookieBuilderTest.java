package utils;

import model.web.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.builder.CookieBuilder;

import static org.assertj.core.api.Assertions.*;

public class CookieBuilderTest {
    @Test
    @DisplayName("쿠키 생성 테스트")
    void IsCookieCreatedWithPath() {
        String key = "JSESSIONID";
        String value = "value";

        Cookie result = CookieBuilder.build(key, value);

        assertThat(result.getKey()).isEqualTo(key);
        assertThat(result.getValue()).isEqualTo(value);
        assertThat(result.getPath()).isEqualTo("Path=/");
    }
}
