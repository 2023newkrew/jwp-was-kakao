package webserver.request;

import utils.IOUtils;
import webserver.request.QueryStringParser.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        String[] startLine = line.split(" ");

        String method = startLine[0];
        String uri = startLine[1];
        String httpVersion = startLine[2];
        HttpRequestHeader httpRequestHeader = HttpRequestHeader.of(method, uri, httpVersion);

        readHttpRequestHeaderLines(br, httpRequestHeader);

        int contentLength = Integer.parseInt(httpRequestHeader.getAttribute("Content-Length").orElse("0"));
        HttpRequestBody httpRequestBody = HttpRequestBody.from(IOUtils.readData(br, contentLength));
        return HttpRequest.of(httpRequestHeader, httpRequestBody);
    }

    private static void readHttpRequestHeaderLines(BufferedReader br, HttpRequestHeader httpRequestHeader) throws IOException {
        String line;
        while (!(line = br.readLine()).equals("")) {
            String[] header = line.split(": ");
            String key = header[0];
            String value = header[1].trim();

            if (key.equals("Cookie")) {
                httpRequestHeader.setCookies(QueryStringParser.parseQueryString(Query.from(value), "; "));
                continue;
            }

            httpRequestHeader.addAttribute(key, value);
        }
    }
}
