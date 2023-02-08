package webserver.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Context {
    private final HttpRequestReader requestReader;
    private final Map<String, Object> keys = new ConcurrentHashMap<>();
    @Setter
    private HttpResponse httpResponse;

    public Context(HttpRequestReader requestReader) {
        this.requestReader = requestReader;
    }

    public Object get(String key) {
        return keys.get(key);
    }

    public void set(String key, Object value) {
        keys.put(key, value);
    }

    public <T> T bindQuery(Class<T> clazz) {
        return this.requestReader.bindQuery(clazz);
    }

    public <T> T bindBody(Class<T> clazz) {
        return this.requestReader.bindBody(clazz);
    }

    public <T> T bindJson(Class<T> clazz) {
        return this.requestReader.bindJson(clazz);
    }
}
