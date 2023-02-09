package http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpSession {
    private final String id;
    private final Map<String, Object> values = new HashMap<>();
    private boolean isValidate = true;

    public HttpSession(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Object getAttribute(String name) {
        return values.get(name);
    }

    public boolean isValidate() {
        return isValidate;
    }

    public void setAttribute(String name, final Object value) {
        values.put(name, value);
    }

    public void removeAttribute(String name) {
        values.remove(name);
    }

    public void invalidate() {
        values.clear();
        isValidate = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpSession that = (HttpSession) o;
        return isValidate == that.isValidate && Objects.equals(id, that.id) && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, values, isValidate);
    }

    @Override
    public String toString() {
        return "HttpSession{" +
                "id='" + id + '\'' +
                ", values=" + values +
                ", isValidate=" + isValidate +
                '}';
    }
}
