package http;

import java.util.*;

public class HttpRequestParams {
    private final Map<String, String> params = new HashMap<>();

    public HttpRequestParams() {
    }

    public HttpRequestParams(Map<String, String> parameters) {
        params.putAll(parameters);
    }

    public String getParameter(String name) {
        return params.get(name);
    }

    public void setParameter(String name, String value) {
        params.put(name, value);
    }

    public Map<String, String> getParameters() {
        return new HashMap<>(params);
    }

    public void setParameters(Map<String, String> parameters) {
        params.putAll(parameters);
    }

    public boolean hasParameter(String name) {
        return params.containsKey(name);
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

    public int size() {
        return params.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestParams that = (HttpRequestParams) o;
        return Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }

    @Override
    public String toString() {
        return "HttpRequestParams{" +
                "params=" + params +
                '}';
    }
}
