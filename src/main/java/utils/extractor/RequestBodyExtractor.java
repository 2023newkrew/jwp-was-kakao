package utils.extractor;

import com.fasterxml.jackson.core.type.TypeReference;
import exception.DataCannotLoadedException;
import exception.UnsupportedContentTypeException;
import lombok.experimental.UtilityClass;
import model.enumeration.ContentType;
import utils.factory.ObjectMapperFactory;
import utils.utils.IOUtils;

import java.io.BufferedReader;
import java.util.Map;

import static constant.HeaderConstant.*;
import static utils.parser.QueryStringParser.*;

@UtilityClass
public class RequestBodyExtractor {
    public Map<String, String> extract(Map<String, String> requestHeaders, BufferedReader bufferedReader) {
        switch (ContentType.of(requestHeaders.get(CONTENT_TYPE))) {
            case APPLICATION_JSON:
                return convertJsonBodyToMap(requestHeaders, bufferedReader);
            case APPLICATION_URL_ENCODED:
                return convertQueryStringToMap(requestHeaders, bufferedReader);
            default:
                throw new UnsupportedContentTypeException();
        }
    }

    private Map<String, String> convertJsonBodyToMap(Map<String, String> requestHeaders, BufferedReader bufferedReader) {
        try {
            return ObjectMapperFactory.getInstance()
                    .readValue(readData(bufferedReader, getContentLength(requestHeaders)),
                            new TypeReference<>() {});
        } catch (Exception e) {
            throw new DataCannotLoadedException(e);
        }
    }

    private String readData(BufferedReader bufferedReader, int length) {
        return IOUtils.readData(bufferedReader, length);
    }

    private int getContentLength(Map<String, String> requestHeaders) {
        return Integer.parseInt(requestHeaders.get(CONTENT_LENGTH));
    }

    private Map<String, String> convertQueryStringToMap(Map<String, String> requestHeaders, BufferedReader bufferedReader) {
        return parseQueryString(readData(bufferedReader, getContentLength(requestHeaders)));
    }
}
