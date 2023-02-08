package logics;

import logics.post.LoginController;
import logics.post.UserCreateController;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.util.Objects;

/**
 * PostController makes appropriate response when getting "POST request"
 */
public class PostController extends Controller {
    private final Service service;
    private final UrlMatcher urlMatcher = new UrlMatcher();
    public PostController(Service service){
        this.service = service;
        urlMatcher.addMatch("/user/create", new UserCreateController());
        urlMatcher.addMatch("/user/login", new LoginController());
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
