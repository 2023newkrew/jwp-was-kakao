package application.user;

import application.JinBaseE2ETest;
import application.db.DataBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserE2ETest extends JinBaseE2ETest {
    @Test
    void GET_방식으로_USER_회원가입() {
        //given
        String socketInput = String.join("\r\n",
                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Accept: */*",
                "",
                "");

        //when
        String socketOutput = getSocketOutputFromWebServer(socketInput);

        //then
        String expectedFirstLine = "HTTP/1.1 302 FOUND";
        String expectedLocationHeader = "Location: http://localhost:8080/index.html";

        assertThat(DataBase.findAll()).hasSize(1);

        assertThat(socketOutput)
                .startsWith(expectedFirstLine)
                .contains(expectedLocationHeader);
    }

    @Test
    void POST_방식으로_form_으로부터_User_회원가입(){
        //given
        String socketInput = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 92 ",
                "Content-Type: application/x-www-form-urlencoded ",
                "Content-Type: text/plain ",
                "Accept: */*",
                "",
                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com",
                "",
                "");

        //when
        String socketOutput = getSocketOutputFromWebServer(socketInput);

        //then
        String expectedFirstLine = "HTTP/1.1 302 FOUND";
        String expectedLocationHeader = "Location: http://localhost:8080/index.html";

        assertThat(DataBase.findAll()).hasSize(1);
        assertThat(socketOutput)
                .startsWith(expectedFirstLine)
                .contains(expectedLocationHeader);
    }
}
