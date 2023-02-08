package webserver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import model.User;
import model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try {
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/");
            loader.setSuffix("");
            Handlebars handlebars = new Handlebars(loader);

            Template template = handlebars.compile("./templates/user/list.html");

            List<UserDto> users = new ArrayList<>();
            users.add(UserDto.of(new User("cu", "password", "이동규", "brainbackdoor@gmail.com"), 1));
            users.add(UserDto.of(new User("haha", "password", "haha", "haha@gmail.com"), 2));
            String profilePage = template.apply(users);
            logger.debug("ProfilePage : {}", profilePage);
        } catch(Exception e){
            e.printStackTrace();

        }


        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            // 강의에서 Thread Pool을 사용하는 것을 고려해보라고 하셨지만, 일단 step 1에서는 요청이 오는대로 쓰레드를 생성하는 방식으로 구현하였습니다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                Thread thread = new Thread(new RequestHandler(connection));
                thread.start();
            }
        }
    }
}
