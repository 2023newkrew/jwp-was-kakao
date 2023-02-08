package infra.utils.request;

import static infra.utils.IOUtils.readData;

import infra.utils.Api;
import infra.utils.Extension;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class MyRequest {

    private final HttpMethod method;
    private final String path;
    private final MyRequestMap params;
    private final MyRequestMap headers;
    private final MyRequestMap queryParams;
    private final Extension extension;
    private final Api api;
    private final Map<Class<?>, Object> context = new HashMap<>();

    protected MyRequest(HttpMethod method, String path, MyRequestMap params, MyRequestMap headers,
                      MyRequestMap queryParams,
                      Extension extension) {
        this.method = method;
        this.path = path;
        this.params = params;
        this.headers = headers;
        this.queryParams = queryParams;
        this.extension = extension;
        this.api = new Api(path, method);
    }


    public static MyRequestBuilder builder(BufferedReader bufferedReader) throws IOException {
        return new MyRequestBuilder(bufferedReader);
    }

    public void putContext(Class<?> type, Object object){
        context.put(type, object);
    }

    public Object getContext(Class<?> type){
        return context.get(type);
    }

    public boolean isStatic() {
        return extension.isStatic();
    }

    public String getParam(String key) {
        return this.params.get(key);
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public String getQueryParam(String key) {
        return this.queryParams.get(key);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Api getApi() {
        return this.api;
    }

    public static class MyRequestBuilder {
        private final MyRequestMap params;
        private final MyRequestMap headers;
        private final MyRequestMap queryParams;
        private final BufferedReader bufferedReader;

        public MyRequestBuilder(BufferedReader bufferedReader) {
            this.bufferedReader = bufferedReader;
            params = new MyParams();
            headers = new MyHeaders();
            queryParams = new MyParams();
        }

        public MyRequest build() throws IOException {
            String line = bufferedReader.readLine();
            String[] firstLine = line.split(" ");
            HttpMethod method = HttpMethod.valueOf(firstLine[0]);
            String path = parsePath(firstLine[1]);
            if (line.contains("?")) {
                putQueryParams(line);
            }
            line = bufferedReader.readLine();
            while (!isFinalHeaderLine(line)) {
                putHeader(line);
                line = bufferedReader.readLine();
            }
            if (headers.contains(HttpHeaders.CONTENT_LENGTH)) {
                putBody(bufferedReader);
            }
            String[] tokens = path.split("\\.");
            Extension extension = new Extension(tokens[tokens.length - 1]);
            return new MyRequest(method, path, params, headers, queryParams, extension);
        }

        private String parsePath(String token) {
            if (token.contains("?")) {
                return token.split("\\?")[0];
            }
            return token;
        }

        private void putBody(BufferedReader bufferedReader) throws IOException {
            String body = readData(bufferedReader, Integer.parseInt(headers.get(HttpHeaders.CONTENT_LENGTH)));
            Arrays.stream(body.split("&")).forEach(str -> {
                var keyAndValue = str.split("=");
                this.params.put(keyAndValue[0], keyAndValue[1]);
            });
        }

        private boolean isFinalHeaderLine(String line) {
            return (Objects.isNull(line) || line.equals(""));
        }

        private void putHeader(String line) {
            String[] keyAndValue = line.split(": ");
            headers.put(keyAndValue[0], keyAndValue[1].trim());
        }

        private void putQueryParams(String line) {
            String[] tokens = line.split("\\?");
            Arrays.stream(tokens[1].split("&")).forEach(str -> {
                var keyAndValue = str.split("=");
                queryParams.put(keyAndValue[0], keyAndValue[1]);
            });

        }
    }
}
