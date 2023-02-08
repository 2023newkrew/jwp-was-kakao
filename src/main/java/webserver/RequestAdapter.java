package webserver;

import controller.RequestController;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import model.Request;
import model.Response;
import org.springframework.http.HttpMethod;

public class RequestAdapter {
    private final RequestController requestController = new RequestController();

    public void mapHandler(Request request, Response response) throws URISyntaxException, IOException {

        if (request.getMethod().equals(HttpMethod.GET)) {
            requestController.sendPage(request, response);
        }

        if (request.getMethod().equals(HttpMethod.POST)) {

            if (request.getPath().equals("/user/create")) {
                requestController.createUser(request, response);
                return;
            }

            if (request.getPath().equals("/user/login")) {
                requestController.login(request, response);
                return;
            }

        }
    }
}
