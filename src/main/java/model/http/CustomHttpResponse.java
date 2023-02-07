package model.http;

public class CustomHttpResponse {

    private final CustomHttpStatus httpStatus;
    private final CustomHttpHeader headers;
    private final String body;

    public CustomHttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CustomHttpHeader getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    private CustomHttpResponse(Builder builder) {
        this.httpStatus = builder.httpStatus;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public static class Builder {
        private CustomHttpStatus httpStatus;
        private CustomHttpHeader headers;
        private String body;

        public Builder() {
        }

        public Builder(CustomHttpStatus httpStatus, CustomHttpHeader headers, String body) {
            this.httpStatus = httpStatus;
            this.headers = headers;
            this.body = body;
        }

        public Builder httpStatus(CustomHttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder headers(CustomHttpHeader headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public CustomHttpResponse build() {
            return new CustomHttpResponse(this);
        }
    }
}
