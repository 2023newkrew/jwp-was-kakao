package webserver.http;

import http.exception.HttpRequestFormatException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpRequestHeaderParser {

    public Map<String, List<String>> parse(String headerString) {
        return parse(List.of(headerString.split("\r\n")));
    }

    public Map<String, List<String>> parse(List<String> headerLines) {
        if (headerLines == null || headerLines.isEmpty()) {
            return Map.of();
        }

        Map<String, List<String>> headers = new LinkedHashMap<>();

        headerLines.stream()
                .filter(headerLine -> !headerLine.isBlank())
                .map(this::parseHeaderLine)
                .forEach(header -> headers.put(header.getKey(), header.getValue()));

        return headers;
    }

    private Map.Entry<String, List<String>> parseHeaderLine(String headerLine) {
        if (!headerLine.contains(":")) {
            throw new HttpRequestFormatException();
        }

        String[] nameAndValues = headerLine.split(":", 2);
        String headerName = nameAndValues[0];
        String valueString = nameAndValues[1];

        if (valueString.isBlank()) {
            return new AbstractMap.SimpleEntry<>(headerName, Collections.emptyList());
        }

        List<String> values = Stream.of(valueString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        return new AbstractMap.SimpleEntry<>(headerName, values);
    }
}
