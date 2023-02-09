package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class UriTest {
    @DisplayName("확장자나 query string 이 없는 uri")
    @ParameterizedTest
    @ValueSource(strings = {"/", "/user", "/user/create"})
    void uriWithNone(String literalUri) {
        Uri uri = new Uri(literalUri);

        assertThat(uri.getUri()).isEqualTo(literalUri);
        assertThat(uri.getPath()).isEqualTo(literalUri);
        assertThat(uri.getExtension()).isEmpty();
        assertThat(uri.getParams()).isEmpty();
    }

    @DisplayName("확장자가 있는 uri")
    @ParameterizedTest
    @CsvSource(value = {
            "/index.html;html",
            "/user/index.css;css",
            "/user/create/index.js;js"
    }, delimiter = ';')
    void uriWithExtension(String literalUri, String extension) {

        Uri uri = new Uri(literalUri);

        assertThat(uri.getUri()).isEqualTo(literalUri);
        assertThat(uri.getPath()).isEqualTo(literalUri);
        assertThat(uri.getExtension().orElse("")).isEqualTo(extension);
        assertThat(uri.getParams()).isEmpty();
    }

    @DisplayName("query string 이 있는 uri")
    @ParameterizedTest
    @CsvSource(value = {
            "/index?username=jay;/index;username;jay",
            "/user/index?username=jayden;/user/index;username;jayden",
            "/user/create/index?username=james;/user/create/index;username;james"
    }, delimiter = ';')
    void uriWithQueryString(String literalUri, String path, String key, String value) {
        Uri uri = new Uri(literalUri);

        assertThat(uri.getUri()).isEqualTo(literalUri);
        assertThat(uri.getPath()).isEqualTo(path);
        assertThat(uri.getParams()).isPresent();
        assertThat(uri.getParams().get().get(key).orElse("")).isEqualTo(value);
        assertThat(uri.getExtension()).isEmpty();
    }

    @DisplayName("query string 이 있는 uri")
    @ParameterizedTest
    @CsvSource(value = {
            "/index.html?username=jay;/index.html;html;username;jay",
            "/user/index.css?username=jayden;/user/index.css;css;username;jayden",
            "/user/create/index.js?username=james;/user/create/index.js;js;username;james"
    }, delimiter = ';')
    void uriWithExtensionAndQueryString(String literalUri, String path, String extension, String key, String value) {
        Uri uri = new Uri(literalUri);

        assertThat(uri.getUri()).isEqualTo(literalUri);
        assertThat(uri.getPath()).isEqualTo(path);
        assertThat(uri.getParams()).isPresent();
        assertThat(uri.getParams().get().get(key).orElse("")).isEqualTo(value);
        assertThat(uri.getExtension()).isPresent();
        assertThat(uri.getExtension().get()).isEqualTo(extension);
    }
}