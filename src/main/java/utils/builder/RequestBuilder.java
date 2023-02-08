package utils.builder;

import lombok.experimental.UtilityClass;
import model.dto.request.HttpRequestKeyValue;
import model.request.HttpRequest;
import model.request.properties.HttpRequestFirstLineProperties;
import model.request.properties.RequestBody;
import model.request.properties.RequestHeaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RequestBuilder {
    public HttpRequest getHttpRequest(BufferedReader bufferedReader) throws IOException {
        Map<String, String> requestHeaderMap = new HashMap<>();
        String line = bufferedReader.readLine();
        HttpRequestFirstLineProperties httpRequestFirstLineProperties = new HttpRequestFirstLineProperties(line);

        while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
            HttpRequestKeyValue httpRequestKeyValue = new HttpRequestKeyValue(line);
            requestHeaderMap.put(
                    httpRequestKeyValue.getKey(),
                    httpRequestKeyValue.getValue()
            );
        }

        return new HttpRequest(
                httpRequestFirstLineProperties,
                RequestHeaders.of(requestHeaderMap),
                RequestBody.of(requestHeaderMap, bufferedReader)
        );
    }
}
