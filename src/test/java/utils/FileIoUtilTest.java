package utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.infra.util.FileIoUtil;

public class FileIoUtilTest {
    private static final Logger log = LoggerFactory.getLogger(FileIoUtilTest.class);

    @Test
    void loadFileFromClasspath() throws Exception {
        byte[] body = FileIoUtil.loadFileFromClasspath("/index.html");
        log.debug("file : {}", new String(body));
    }
}
