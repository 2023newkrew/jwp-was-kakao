package logics.controller;

import logics.controller.support.UrlMatcher;
import logics.controller.post.LoginController;
import logics.controller.post.UserCreateController;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.util.Objects;

/**
 * PostController makes appropriate response when getting "POST request"
 */
public class PostController implements Controller {
    public static final Controller instance = new PostController();
    private final UrlMatcher urlMatcher = new UrlMatcher();
    private PostController(){
        urlMatcher.addMatch("/user/create", UserCreateController.instance);
        urlMatcher.addMatch("/user/login", LoginController.instance);
    }

    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        Controller matchedController = urlMatcher.getMatchingUrl(httpRequest.getURI().getPath());
        if (Objects.nonNull(matchedController)){
            return matchedController.makeResponse(httpRequest);
        }
        return new HttpResponseVersion1().setResponseCode(404);
    }

}
