package utils.extractor;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.experimental.UtilityClass;
import model.enumeration.ContentType;
import utils.factory.ObjectMapperFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static constant.HeaderConstant.*;
import static utils.parser.QueryStringParser.*;
import static utils.utils.IOUtils.*;

@UtilityClass
public class RequestBodyExtractor {
    public Map<String, String> extract(Map<String, String> requestHeaders, BufferedReader bufferedReader) {
        try {
            switch (ContentType.of(requestHeaders.get(CONTENT_TYPE))) {
                case APPLICATION_JSON:
                    return ObjectMapperFactory.getInstance()
                            .readValue(
                                    readData(
                                            bufferedReader,
                                            Integer.parseInt(requestHeaders.get(CONTENT_LENGTH))
                                    ),
                                    new TypeReference<Map<String, String>>() {
                                    }
                            );
                case APPLICATION_URL_ENCODED:
                    return parseQueryString(
                            readData(bufferedReader, Integer.parseInt(requestHeaders.get(CONTENT_LENGTH)))
                    );
                default:
                    throw new RuntimeException("지원하지 않는 Content-Type 입니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
