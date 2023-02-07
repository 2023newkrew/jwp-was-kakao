package model.request;

import com.fasterxml.jackson.core.type.TypeReference;
import utils.IOUtils;
import utils.ObjectMapperFactory;
import utils.QueryStringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static constant.ContentTypeConstant.*;
import static constant.HeaderConstant.*;
import static utils.IOUtils.*;
import static utils.QueryStringParser.*;

public class RequestBodyExtractor {
    public static Map<String, String> extract(Map<String, String> requestHeaders, BufferedReader bufferedReader) {
        try {
            switch (requestHeaders.get(CONTENT_TYPE)) {
                case APPLICATION_JSON:
                    return ObjectMapperFactory.getInstance()
                            .readValue(
                                    readData(
                                            bufferedReader,
                                            Integer.parseInt(requestHeaders.get(CONTENT_LENGTH))
                                    ),
                                    new TypeReference<Map<String, String>>() {}
                            );
                case APPLICATION_FORM_URL_ENCODED:
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
