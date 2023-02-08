package logics.post;

import logics.Controller;
import logics.Service;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

public class UserCreateController extends Controller {
    private final Service service = new Service();
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        service.updateUser(httpRequest.getBody());
        return new HttpResponseVersion1().setResponseCode(302)
                .setHeader("Location", "/index.html");
    }
}
