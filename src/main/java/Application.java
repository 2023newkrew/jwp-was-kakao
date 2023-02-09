import controller.UserController;
import exception.handler.GlobalExceptionHandler;
import framework.WebServer;
import framework.requestmapper.ExceptionHandlerMapper;
import framework.requestmapper.HandlerMapper;

import java.util.List;

public class Application {
    public static void main(String[] args) throws Exception {
        HandlerMapper.getInstance().setHandlers(List.of(UserController.getInstance()));
        ExceptionHandlerMapper.getInstance().setHandlers(List.of(GlobalExceptionHandler.getInstance()));
        WebServer.run(args);
    }
}
