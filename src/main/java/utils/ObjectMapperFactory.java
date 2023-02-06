package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import webserver.controller.UserController;
import webserver.dao.UserDao;
import webserver.service.UserService;

@UtilityClass
public class ObjectMapperFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectMapper getInstance() {
        return objectMapper;
    }
}
