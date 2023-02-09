package webserver.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FileTypeTest {

    @DisplayName("파일 확장자를 통해 알맞은 FileType을 받을 수 있다. - HTML")
    @Test
    void findTypeHTML() {
        // given & when
        FileType HTML = FileType.findType("html");

        // then
        assertThat(HTML).isEqualTo(FileType.HTML);
    }

    @DisplayName("파일 확장자를 통해 알맞은 FileType을 받을 수 있다. - EOT")
    @Test
    void findTypeEOT() {
        // given & when
        FileType EOT = FileType.findType("eot");

        // then
        assertThat(EOT).isEqualTo(FileType.EOT);
    }

    @DisplayName("파일 확장자로 알맞은 FileType이 없으면 핸들러로 간주한다.")
    @Test
    void findHandler() {
        // given & when
        FileType handler = FileType.findType("/users");

        // then
        assertThat(handler).isEqualTo(FileType.HANDLER);
    }
}