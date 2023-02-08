package utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.infra.util.IoUtil;

import java.io.BufferedReader;
import java.io.StringReader;

public class IoUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(IoUtilTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IoUtil.readData(br, data.length()));
    }
}
