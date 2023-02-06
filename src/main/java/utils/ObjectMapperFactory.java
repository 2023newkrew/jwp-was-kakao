package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectMapper getInstance() {
        return objectMapper;
    }
}
