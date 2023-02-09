package web.exception.handler;

import web.exception.AuthErrorCode;
import web.exception.BusinessException;
import framework.annotation.MyExceptionHandler;
import framework.controller.ExceptionController;
import framework.response.Response;
import framework.utils.FileIoUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GlobalExceptionHandler implements ExceptionController {

    private final Map<Class<? extends Throwable>, Method> map = new HashMap<>();

    private static class LazyHolder {
        public static final GlobalExceptionHandler INSTANCE = new GlobalExceptionHandler();
    }

    private GlobalExceptionHandler() {
        List<Method> methodList = List.of(this.getClass().getMethods());
        for (Method method: methodList) {
            MyExceptionHandler myRequestMapping = method.getAnnotation(MyExceptionHandler.class);
            if (myRequestMapping != null) {
                map.put(myRequestMapping.value(), method);
            }
        }
    }

    public static GlobalExceptionHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Response handleRequest(Throwable throwable) {
        try {
            return (Response) map.get(throwable.getClass()).invoke(GlobalExceptionHandler.getInstance(), throwable);
        } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canHandle(Throwable throwable) {
        return map.containsKey(throwable.getClass());
    }

    @MyExceptionHandler(RuntimeException.class)
    public Response handleRuntimeException(RuntimeException runtimeException) {
        return Response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR).body("500 Internal Server Error").build();
    }

    @MyExceptionHandler(BusinessException.class)
    public Response handleBusinessException(BusinessException businessException) {
        if (businessException.getErrorCode().equals(AuthErrorCode.INVALID_CREDENTIAL)) {
            return handleLoginException();
        }
        return Response.setStatus(businessException.getErrorCode().getHttpStatus())
                .body(businessException.getErrorCode().getMessage())
                .build();
    }

    private Response handleLoginException() {
        String body;
        try {
            body = new String(Objects.requireNonNull(FileIoUtils.loadFileFromClasspath("templates/user/login_failed.html")), StandardCharsets.UTF_8);
        } catch (IOException | NullPointerException | URISyntaxException e) {
            e.printStackTrace();
            return Response.unauthorized().body("Unauthorized").build();
        }
        return Response.unauthorized().body(body).build();
    }

}
