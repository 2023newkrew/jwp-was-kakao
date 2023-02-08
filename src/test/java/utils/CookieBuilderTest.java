package utils;

import model.web.Cookie;
import org.assertj.core.api.Assertions;
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

        assertThat(result.key()).isEqualTo(key);
        assertThat(result.value()).isEqualTo(value + "; Path=/");
    }
}
