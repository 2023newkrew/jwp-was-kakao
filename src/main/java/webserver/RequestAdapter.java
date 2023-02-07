package webserver;

import controller.RequestController;
import java.io.DataOutputStream;
import java.net.URISyntaxException;
import model.RequestInfo;
import org.springframework.http.HttpMethod;

public class RequestAdapter {
    private final RequestController requestController = new RequestController();

    public void mapHandler(RequestInfo request, DataOutputStream dos) throws URISyntaxException {

        if (request.getMethod().equals(HttpMethod.POST) && request.getPath().equals("/user/create")) {
            requestController.createUser(request, dos);
            return;
        }

        requestController.sendPage(request, dos);
    }
}
