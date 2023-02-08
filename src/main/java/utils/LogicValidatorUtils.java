package utils;

import supports.SplitMethodException;
import webserver.RequestHandler;

import java.util.Objects;

public class LogicValidatorUtils {
    private LogicValidatorUtils(){

    }
    public static void checkNull(String s) {
        if (Objects.isNull(s)){
            RequestHandler.logger.error("split 대상이 null입니다.");
            throw new SplitMethodException();
        }
    }
}
