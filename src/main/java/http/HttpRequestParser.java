package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHeader.class);

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String startLine = br.readLine();

        HttpRequestLine httpRequestLine = HttpRequestLine.from(startLine);
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();

        String line = "";
        while(!(line = br.readLine()).equals("")) {
            String[] header = line.split(": ");
            HttpHeaders key = HttpHeaders.valueOf(header[0].toUpperCase().replace("-", "_"));
            String value = header[1].trim();

            httpRequestHeader.addAttribute(key, value);
        }

        String bodyData = IOUtils.readData(
                br,
                Integer.parseInt(httpRequestHeader.getAttribute(HttpHeaders.CONTENT_LENGTH).orElse("0"))
        );

        return new HttpRequest(
                httpRequestLine,
                httpRequestHeader,
                new HttpRequestBody(bodyData)
        );
    }
}
