package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static utils.FileIoUtils.readFile;

public class FileIoUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(FileIoUtilsTest.class);

    @Test
    void loadFileFromClasspath() throws Exception {
        byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
        log.debug("file : {}", new String(body));
    }

    @Test
    void notExistsUrl() {
        Assertions.assertThrows(NullPointerException.class, () -> readFile("./templates/index2.html"));
    }

    @Test
    void defaultUrlTest() throws IOException, URISyntaxException {
        byte[] bytes = readFile("./templates/");
        String s = new String(bytes, StandardCharsets.UTF_8);
        org.assertj.core.api.Assertions.assertThat(s).isEqualTo("Hello world");
    }
}
