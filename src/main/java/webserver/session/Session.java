package webserver.session;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private final String id;
    private final Map<String, Object> values = new HashMap<>();

    public Session(final String id) {
        this.id = id;
    }

    // 현재 세션에 할당되어 있는 고유한 세션 아이디를 반환
    public String getId() {
        return this.id;
    }

    // 현재 세션에 name 인자로 저장되어 있는 객체 값을 찾아 반환
    public Object getAttribute(final String name) {
        return values.get("name");
    }

    // 현재 세션에 value 인자로 전달되는 객체를 name 인자 이름으로 저장
    public void setAttribute(final String name, final Object value) {
        values.put(name, value);
    }

    // 현재 세션에 name 인자로 저장되어 있는 객체 값을 삭제
    public void removeAttribute(final String name) {
        values.remove(name);
    }

    // 현재 세션에 저장되어 있는 모든 값을 삭제
    public void invalidate() {
        values.clear();
    }
}
