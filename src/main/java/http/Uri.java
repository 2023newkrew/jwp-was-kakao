package http;

import http.request.RequestParams;

import java.util.Optional;

public class Uri {
    private final String uri;
    private final String path;
    private final Optional<String> extension;
    private final Optional<RequestParams> params;

    public Uri(String uri) {
        this.uri = uri;
        this.extension = extractExtension();
        this.params = extractParams();
        this.path = extractPath();
    }

    public String getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

    private String extractPath() {
        if (extension.isEmpty() && params.isEmpty()) {
            return uri;
        }
        if (extension.isPresent()) {
            return uri.substring(0, uri.lastIndexOf("/"));
        }
        return uri.substring(0, uri.lastIndexOf("?"));
    }

    public Optional<String> getExtension() {
        return extension;
    }

    public Optional<RequestParams> getParams() {
        return params;
    }

    private Optional<String> extractExtension() {
        String[] splittedUri = uri.split("\\.");
        if (!isSplitted(splittedUri)) {
            return Optional.empty();
        }
        String extention = splittedUri[splittedUri.length - 1];
        if (!ContentType.isFileExtension(extention)) {
            return Optional.empty();
        }
        return Optional.of(extention);
    }
    
    private Optional<RequestParams> extractParams() {
        String[] splittedUri = uri.split("\\?", 2);
        if (!isSplitted(splittedUri)) {
            return Optional.empty();
        }
        String queryString = splittedUri[1];
        return Optional.of(RequestParams.fromQueryString(queryString));
    }

    private boolean isSplitted(String[] splittedUri) {
        return splittedUri.length > 1;
    }
}
