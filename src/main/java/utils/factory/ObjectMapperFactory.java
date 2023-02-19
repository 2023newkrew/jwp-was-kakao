package utils.factory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.Reader;

@UtilityClass
public class ObjectMapperFactory {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectMapper getInstance() {
        return objectMapper;
    }
}
