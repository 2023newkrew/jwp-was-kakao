package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class UriQueryParserTest {

    @DisplayName("uri queryString을 map으로 파싱한다.")
    @Test
    void parse() {
        // given
        String query = "userId=user&password=password&name=davi";

        // when
        Map<String, String> map = UriQueryParser.parse(query);

        // then
        Map<String, String> test = new HashMap<>();
        test.put("userId", "user");
        test.put("password", "password");
        test.put("name", "davi");

        assertThat(map).isEqualTo(test);
    }
}