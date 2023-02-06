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
        String line = br.readLine();
        String[] startLine = line.split(" ");

        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(
                startLine[0],
                startLine[1],
                startLine[2]
        );

        while(!(line = br.readLine()).equals("")) {
            String[] header = line.split(": ");
            HttpHeaders key = HttpHeaders.valueOf(header[0].toUpperCase().replace("-", "_"));
            String value = header[1].trim();

            httpRequestHeader.addAttribute(key, value);
        }

        return new HttpRequest(
                httpRequestHeader,
                new HttpRequestBody(
                        IOUtils.readData(
                                br,
                                Integer.parseInt(httpRequestHeader.getAttribute(HttpHeaders.CONTENT_LENGTH).orElse("0"))
                        )
                )
        );
    }
}
