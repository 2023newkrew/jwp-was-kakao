package logics;

import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

/**
 * PostController makes appropriate response when getting "POST request"
 */
public class PostController extends Controller {
    private final Service service;
    public PostController(Service service){
        this.service = service;
    }
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        if (httpRequest.getURI().getPath().equals("/user/create")){
            service.updateUser(httpRequest.getBody());
            return new HttpResponseVersion1().setResponseCode(302)
                    .setHeader("Location", "/index.html");
        }
        return new HttpResponseVersion1().setResponseCode(404);
    }

}
