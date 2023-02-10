package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import supports.SplitMethodException;
import webserver.RequestHandler;

import java.util.Objects;

public class LogicValidatorUtils {
    public static final Logger logger = LoggerFactory.getLogger(LogicValidatorUtils.class);
    private LogicValidatorUtils(){

    }
    public static void checkNull(String s) {
        if (Objects.isNull(s)){
            logger.error("split 대상이 null입니다.");
            throw new SplitMethodException();
        }
    }
}
