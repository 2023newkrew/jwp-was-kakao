package webserver;

import db.DataBase;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRequestHandler {
    public static byte[] handle(HttpRequest httpRequest) {
        String uri = httpRequest.getUri();
        String body = httpRequest.getBody();

        if (uri.startsWith("/user/create")) {
            Map<String, String> userData = new HashMap<>();

            String[] bodyLine = body.split("&");

            for (String bodyPart : bodyLine) {
                userData.put(bodyPart.split("=")[0], bodyPart.split("=")[1]);
            }

            User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));

            DataBase.addUser(user);
        }

        return "".getBytes();
    }
}
