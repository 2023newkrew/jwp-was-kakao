package webserver;

import application.db.DataBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;
import webserver.utils.FileIoUtils;

import java.io.*;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
//    @Test
//    void socket_out() {
//        // given
//        final var socket = new StubSocket();
//        final var handler = new RequestHandler(socket);
//
//        // when
//        handler.run();
//        String output = socket.output();
//
//        // then
//        String expectedFirstLine = "HTTP/1.1 200 OK";
//        String expectedContentTypeHeader = "Content-Type: application/json;charset=utf-8";
//        String expectedContentLengthHeader = "Content-Length: 11";
//        String expectedBody = "Hello world";
//
//        assertThat(output)
//                .startsWith(expectedFirstLine)
//                .contains(expectedContentTypeHeader)
//                .contains(expectedContentLengthHeader)
//                .endsWith(expectedBody);
//    }
//
//    @Test
//    void index() throws IOException, URISyntaxException {
//        // given
//        final String httpRequest = String.join("\r\n",
//                "GET /index.html HTTP/1.1 ",
//                "Host: localhost:8080 ",
//                "Connection: keep-alive ",
//                "",
//                "");
//
//        final var socket = new StubSocket(httpRequest);
//        final RequestHandler handler = new RequestHandler(socket);
//
//        // when
//        handler.run();
//        String output = socket.output();
//
//        // then
//        String expectedFirstLine = "HTTP/1.1 200 OK";
//        String expectedContentTypeHeader = "Content-Type: text/html;charset=utf-8";
//        String expectedContentLengthHeader = "Content-Length: 7153";
//        String expectedBody = new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));
//
//        assertThat(output)
//                .startsWith(expectedFirstLine)
//                .contains(expectedContentTypeHeader)
//                .contains(expectedContentLengthHeader)
//                .endsWith(expectedBody);
//    }
//
//    @Test
//    @DisplayName("GET 방식으로 form으로 부터 user 생성 테스트")
//    void createUserTestGet(){
//        //given
//        final String httpRequest = String.join("\r\n",
//                "GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1 ",
//                "Host: localhost:8080 ",
//                "Connection: keep-alive ",
//                "Accept: */*",
//                "",
//                "");
//        final var socket = new StubSocket(httpRequest);
//        final RequestHandler handler = new RequestHandler(socket);
//
//        //when
//        handler.run();
//        String output = socket.output();
//
//        //then
//        String expectedFirstLine = "HTTP/1.1 302 FOUND";
//        String expectedLocationHeader = "Location: http://localhost:8080/index.html";
//
//        assertThat(DataBase.findAll()).hasSize(1);
//        assertThat(output)
//                .startsWith(expectedFirstLine)
//                .contains(expectedLocationHeader);
//    }
//
//    @Test
//    @DisplayName("잘못된 queryParams가 들어오면 InvalidQueryParameterException 발생")
//    void InvalidQueryParameterExceptionTest(){
//        //given
//        final String httpRequest = String.join("\r\n",
//                "GET /user/createuserId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1 ",
//                "Host: localhost:8080 ",
//                "Connection: keep-alive ",
//                "Accept: */*",
//                "",
//                "");
//        final var socket = new StubSocket(httpRequest);
//        final RequestHandler handler = new RequestHandler(socket);
//
//        //when
//        handler.run();
//
//        String output = socket.output();
//
//        //then
//        String expectedFirstLine = "HTTP/1.1 400 BAD_REQUEST";
//        String expectedContentTypeHeader = "Content-Type: application/json;charset=utf-8";
//        String expectedContentLengthHeader = "Content-Length: 23";
//        String expectedBody = "Invalid Query Parameter";
//
//        assertThat(output)
//                .startsWith(expectedFirstLine)
//                .contains(expectedContentTypeHeader)
//                .contains(expectedContentLengthHeader)
//                .endsWith(expectedBody);
//    }
//
//    @Test
//    @DisplayName("POST 방식으로 form으로 부터 user 생성 테스트")
//    void createUserTestPost(){
//        //given
//        final String httpRequest = String.join("\r\n",
//                "POST /user/create HTTP/1.1 ",
//                "Host: localhost:8080 ",
//                "Connection: keep-alive ",
//                "Content-Length: 92 ",
////                "Content-Type: application/x-www-form-urlencoded ",
//                "Content-Type: text/plain ",
//                "Accept: */*",
//                "",
//                "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com",
//                "",
//                "");
//        final var socket = new StubSocket(httpRequest);
//        final RequestHandler handler = new RequestHandler(socket);
//
//        //when
//        handler.run();
//        String output = socket.output();
//
//        //then
//        String expectedFirstLine = "HTTP/1.1 302 FOUND";
//        String expectedLocationHeader = "Location: http://localhost:8080/index.html";
//
//        assertThat(DataBase.findAll()).hasSize(1);
//        assertThat(output)
//                .startsWith(expectedFirstLine)
//                .contains(expectedLocationHeader);
//    }
}