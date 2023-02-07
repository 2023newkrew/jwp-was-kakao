package user;

import db.DataBase;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RequestHandler;
import support.StubSocket;
import utils.StringParser;

import java.util.Collection;

public class UserTest {

    @DisplayName("유저의 정보로 회원가입 할 수 있다")
    @Test
    void createUser(){
        // given
        String lenasd = "userId=username&password=password&name=name&email=userInfo%40user.com";
        System.out.println(lenasd.length());
        final String httpRequest = String.join("\r\n",
                "POST /user/create HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: 69",
                "userId=username&password=password&name=name&email=userInfo%40user.com",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, new StringParser());

        handler.run();

        Collection<User> all = DataBase.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
        for (User user : all) {
            Assertions.assertThat(user.getName()).isEqualTo("name");
        }
    }
}
