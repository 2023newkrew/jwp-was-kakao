package logics.controller;

import utils.requests.HttpRequest;
import utils.requests.RequestMethod;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * RootController class does similar role to Controller component of Spring framework.
 * contains sub-controllers like getController and postController.
 */
public class RootController implements Controller {
    public static final Controller instance = new RootController();
    private static final Map<RequestMethod, Controller> requestMethodMatcher = new HashMap<>();
    static{
        requestMethodMatcher.put(RequestMethod.GET, GetController.instance);
        requestMethodMatcher.put(RequestMethod.POST, PostController.instance);
    }
    private RootController(){}

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
        return matchedController.makeResponse(httpRequest);
    }
}
