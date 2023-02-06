package controller;

import annotation.Controller;
import annotation.Mapping;
import annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Mapping(method = RequestMethod.GET, path = "/index.html")
    public static byte[] index() {
        try {
            return FileIoUtils.loadFileFromClasspath("./templates/index.html");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return new byte[]{};
    }

    @Mapping(method = RequestMethod.GET, path = "/")
    public static byte[] socket_out() {
        return "Hello world".getBytes();
    }
}
