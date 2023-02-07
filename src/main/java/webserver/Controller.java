package webserver;

import db.DataBase;
import model.User;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    public HttpResponse requestMapping(HttpRequest httpRequest) throws IOException, URISyntaxException{
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        String uri = httpRequest.getUri();

        // get html
        if (httpMethod == HttpMethod.GET && uri.endsWith(".html")){
            return getHtml(httpRequest);
        }
        // get asset
        else if (httpMethod == HttpMethod.GET &&
                (uri.endsWith(".css") || uri.endsWith(".js") || uri.startsWith("/fonts") || uri.startsWith("/images"))){
            return getAsset(httpRequest);
        }
        // create user
        else if (httpMethod == HttpMethod.POST && uri.startsWith("/user/create")) {
            return createUser(httpRequest);
        }

        // default
        return helloWorld(httpRequest);
    }

    private HttpResponse helloWorld(HttpRequest httpRequest) throws IOException, URISyntaxException {
        byte[] body = "Hello world".getBytes();
        String[] header = {
                "Content-Type: text/html;charset=utf-8 \r\n",
                "Content-Length: " + body.length + " \r\n",
                "\r\n"
        };

        return new HttpResponse("HTTP/1.1", "200 OK", header, body);
    }

    private HttpResponse getHtml(HttpRequest httpRequest) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath("templates" + httpRequest.getUri());
        String[] header = {
                "Content-Type: text/html;charset=utf-8 \r\n",
                "Content-Length: " + body.length + " \r\n",
                "\r\n"
        };

        return new HttpResponse("HTTP/1.1", "200 OK", header, body);
    }

    private HttpResponse getAsset(HttpRequest httpRequest) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath("static" + httpRequest.getUri());
        String[] header = {
                "Content-Type: " + httpRequest.getHeader("Accept") + ";charset=utf-8 \r\n",
                "Content-Length: " + body.length + " \r\n",
                "\r\n"
        };

        return new HttpResponse("HTTP/1.1", "200 OK", header, body);
    }

    private HttpResponse createUser(HttpRequest httpRequest) throws IOException, URISyntaxException {
        Map<String, String> userData = new HashMap<>();
        String[] bodyLine = httpRequest.getBody().split("&");
        for (String bodyPart : bodyLine) {
            userData.put(bodyPart.split("=")[0], bodyPart.split("=")[1]);
        }
        User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
        DataBase.addUser(user);

        byte[] body = {};
        String[] header = {
                "Content-Type: " + httpRequest.getHeader("Accept") + ";charset=utf-8 \r\n",
                "Content-Length: " + body.length + " \r\n",
                "Location: /index.html \r\n",
                "\r\n"
        };

        return new HttpResponse("HTTP/1.1", "302 Found", header, body);
    }


}
