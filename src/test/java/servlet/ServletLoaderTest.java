package servlet;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServletLoaderTest {
    @Test
    void load() {
        ServletLoader.load()
                .forEach((key, servlet) -> {
                    assertThat(servlet.getClass().isAnnotationPresent(ServletMapping.class)).isTrue();
                    assertThat(servlet.getClass().getAnnotation(ServletMapping.class).uri()).isEqualTo(key);
                });
    }
}