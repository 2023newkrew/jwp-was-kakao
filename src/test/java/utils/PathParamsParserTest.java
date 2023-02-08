package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import was.utils.PathParamsParser;

class PathParamsParserTest {

    @DisplayName("요청한 url에서 path와 params를 분리한다")
    @Test
    void parseTest() {
        String url = "/user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com";

        PathParamsParser pathParamsParser = new PathParamsParser(url);

        Assertions.assertThat(pathParamsParser.getParams()).isNotEmpty();
        Assertions.assertThat(pathParamsParser.getPath()).isEqualTo("/user/create");
    }

    @DisplayName("요청한 url에서 params가 없는 path는 path만 분리한다")
    @Test
    void parseWithputQueryStringTest() {
        String url = "/user/create";

        PathParamsParser pathParamsParser = new PathParamsParser(url);

        Assertions.assertThat(pathParamsParser.getParams()).isEmpty();
        Assertions.assertThat(pathParamsParser.getPath()).isEqualTo("/user/create");
    }
}
