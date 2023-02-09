package http;

import java.util.*;

public class HttpRequestParams {
    private final Map<String, String> params = new HashMap<>();

    public HttpRequestParams() {
    }

    public HttpRequestParams(String parameters) {
        parse(parameters);
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

    private void parse(String parameters) {
        String[] splitParam = parameters.split("&");

        Arrays.stream(splitParam)
                .filter(param -> !param.isBlank())
                .map(param -> param.split("=", 2))
                .map(this::convertToEntry)
                .forEach(paramEntry -> params.put(paramEntry.getKey(), paramEntry.getValue()));
    }

    private Map.Entry<String, String> convertToEntry(String[] nameAndValue) {
        if (nameAndValue.length == 1) {
            return new AbstractMap.SimpleEntry<>(nameAndValue[0], "");
        } else {
            return new AbstractMap.SimpleEntry<>(nameAndValue[0], nameAndValue[1]);
        }
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
