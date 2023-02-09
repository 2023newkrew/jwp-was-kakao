package logics;

import utils.requests.HttpRequest;
import utils.requests.RequestMethod;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * RootController class does similar role to Controller component of Spring framework.
 * Contains sub-controllers like getController and postController.
 */
public class RootController extends Controller {
    private static final Service service = new Service();
    private static final Map<RequestMethod, Controller> requestMethodMatcher = new HashMap<>();
    static{
        requestMethodMatcher.put(RequestMethod.GET, new GetController());
        requestMethodMatcher.put(RequestMethod.POST, new PostController(service));
    }

    /**
     * make response when httpRequest is given.
     * @param httpRequest from clients.
     * @return HttpResponse which can directly respond to client.
     * @throws IllegalArgumentException when request contains wrong information such as inappropriate path.
     */
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        Controller matchedController = requestMethodMatcher.get(httpRequest.getRequestMethod());
        if (Objects.isNull(matchedController)){
            return new HttpResponseVersion1().setResponseCode(405);
        }
        return requestMethodMatcher.get(httpRequest.getRequestMethod()).makeResponse(httpRequest);
    }
}
