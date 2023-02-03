package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class QueryStringParserTest {
    @Test
    @DisplayName("쿼리 스트링 파싱 테스트")
    void Should_ParseQueryString_Successfully() {
        // given
        String queryString = "username=noah&age=26&gender=male";

        // when
        Map<String, String> result = QueryStringParser.parseQueryString(queryString);

        // then
        assertThat(result.get("username")).isEqualTo("noah");
        assertThat(result.get("age")).isEqualTo("26");
        assertThat(result.get("gender")).isEqualTo("male");
    }
}
