package webserver.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HttpSessionsTest {

    @DisplayName("세션을 생성할 수 있다.")
    @Test
    void create() {
        // given & when
        HttpSession httpSession = HttpSessions.create();

        // then
        assertThat(httpSession).isNotNull();
    }

    @DisplayName("세션을 생성하고 세션 ID를 기반으로 세션 저장소에서 가져올 수 있다.")
    @Test
    void get() {
        // given
        HttpSession httpSession = HttpSessions.create();
        String id = httpSession.getId();

        // when
        HttpSession foundSession = HttpSessions.get(id);

        // then
        assertThat(httpSession).isEqualTo(foundSession);
    }

    @DisplayName("없는 세션 ID에 접근하면 null을 반환한다")
    @Test
    void getSessionNull() {
        // given & when
        HttpSession httpSession = HttpSessions.get("Not-assigned-key");

        // then
        assertThat(httpSession).isNull();
    }

    @DisplayName("특정 세션에 저장한 값을 가져올 수 있다.")
    @Test
    void findValue() {
        // given
        HttpSession httpSession = HttpSessions.create();
        httpSession.setAttribute("Integer", 1);
        httpSession.setAttribute("String", "hello");
        httpSession.setAttribute("Long", 2L);

        String id = httpSession.getId();

        // when
        Integer value1 = (Integer) HttpSessions.findValue(id, "Integer");
        String value2 = (String) HttpSessions.findValue(id, "String");
        Long value3 = (Long) HttpSessions.findValue(id, "Long");

        // then
        assertThat(value1).isEqualTo(1);
        assertThat(value2).isEqualTo("hello");
        assertThat(value3).isEqualTo(2L);
    }

    @DisplayName("세션에 저장이 안 되어있는 값은 Null로 가져온다")
    @Test
    void findValueNull() {
        // given
        String invalidId = "invalid-id";

        HttpSession httpSession = HttpSessions.create();
        String id = httpSession.getId();

        // when
        Object value1 = HttpSessions.findValue(invalidId, "Key");
        Object value2 = HttpSessions.findValue(id, "Key");

        // then
        assertThat(value1).isNull();
        assertThat(value2).isNull();
    }
}
