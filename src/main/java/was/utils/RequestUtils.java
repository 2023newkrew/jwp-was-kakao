package was.utils;

import controller.CssController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.annotation.RequestMethod;
import was.domain.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static Request getRequest(InputStream inputStream){
        BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));

        try{
            String requestLine = reader.readLine();
            String method = requestLine.split(" ")[0];
            PathParamsParser pathParamsParser = new PathParamsParser(requestLine.split(" ")[1]);
            Map<String, String> header = getHeader(reader);
            String requestBody = getBody(reader);

            return new Request(RequestMethod.valueOf(method), pathParamsParser.getPath(), pathParamsParser.getParams(), requestBody);
        } catch(IOException e){
            logger.error(e.getMessage());
            return null;
        }
    }

    private static Map<String, String> getHeader(BufferedReader reader) throws IOException {
        return getLines(reader).stream()
                .map(it -> it.split(": ", 2))
                .collect(Collectors.toMap(it -> it[0], it -> it[1]));
    }

    private static List<String> getLines(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line = reader.readLine();
        while (line != null && line.equals("")) {
            lines.add(line);
            line = reader.readLine();
        }
        return lines;
    }

    private static String getBody(BufferedReader reader) throws IOException {
        return String.join("\r\n", getLines(reader));
    }
}
