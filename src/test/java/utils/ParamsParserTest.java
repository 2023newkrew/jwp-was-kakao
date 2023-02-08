package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import was.utils.ParamsParser;

import java.util.Map;

class ParamsParserTest {

    @DisplayName("http 요청의 Query String의 parameter Map을 반환한다")
    @Test
    void parseTest() {
        String queryString = "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com";

        Map<String, String> params = ParamsParser.from(queryString).getParams();

        Assertions.assertThat(params).containsEntry("userId", "cu")
                .containsEntry("password", "password")
                .containsEntry("name", "%EC%9D%B4%EB%8F%99%EA%B7%9C")
                .containsEntry("email", "brainbackdoor%40gmail.com");
    }
}
