package webserver;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {
    private final InputStream inputStream;
    private final RequestParser requestParser;

    private HttpMethod method;
    private String url;
    private String queryString;
    private String protocol;
    private Map<String, String> headerMap = new HashMap<>();

    private int contentLength;
    private String contentType;
    private Map<String, String> parameterMap = new HashMap<>();

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
        this.requestParser = new RequestParser();
        requestParser.parse();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHeader(String name) {
        return headerMap.getOrDefault(name, null);
    }

    public String getParameter(String name) {
        return parameterMap.getOrDefault(name, null);
    }

    private class RequestParser {
        private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        private void parse() {
            try {
                parseStartLine();
                parseHeaders();

                if ("application/x-www-form-urlencoded".equals(contentType)) {
                    queryString = readBody();
                }

                if (queryString != null) {
                    parseParameters();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void parseStartLine() throws IOException {
            String[] tokens = bufferedReader.readLine().split(" ");

            method = HttpMethod.valueOf(tokens[0]);
            protocol = tokens[2];

            String[] requestTarget = tokens[1].split("\\?");
            url = requestTarget[0];
            if (requestTarget.length > 1) {
                queryString = requestTarget[1];
            }
        }

        private void parseHeaders() throws IOException {
            String line;

            while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
                String[] tokens = line.split(": ");
                String headerName = tokens[0];
                String headerValue = tokens[1].trim();
                headerMap.put(headerName, headerValue);
            }

            contentLength = Optional.ofNullable(getHeader("Content-Length"))
                    .map(Integer::parseInt)
                    .orElse(-1);
            contentType = getHeader("Content-Type");
        }

        private String readBody() throws IOException {
            return IOUtils.readData(bufferedReader, contentLength);
        }

        private void parseParameters() {
            Arrays.stream(queryString.split("&"))
                    .map(param -> param.split("="))
                    .forEach(split -> parameterMap.put(split[0], split[1]));
        }
    }
}
