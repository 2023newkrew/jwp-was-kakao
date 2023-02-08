package logics;

import utils.requests.HttpRequest;
import utils.requests.RequestMethod;
import utils.response.HttpResponse;

/**
 * RootController class does similar role to Controller component of Spring framework.
 * Contains sub-controllers like getController and postController.
 */
public class RootController extends Controller {
    private final Service service = new Service();
    private final Controller getController = new GetController();
    private final Controller postController = new PostController(service);

    /**
     * make response when httpRequest is given.
     * @param httpRequest from clients.
     * @return HttpResponse which can directly respond to client.
     * @throws IllegalArgumentException when request contains wrong information such as inappropriate path.
     */
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        return makeResponseByMethod(httpRequest);
    }

    private HttpResponse makeResponseByMethod(HttpRequest httpRequest) {
        if (httpRequest.getRequestMethod().equals(RequestMethod.GET)) {
            return getController.makeResponse(httpRequest);
        }
        return postController.makeResponse(httpRequest);
    }

}
