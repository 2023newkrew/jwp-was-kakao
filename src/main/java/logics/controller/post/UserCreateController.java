package logics.controller.post;

import logics.controller.Controller;
import logics.Service.UserService;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

/**
 * make response when receiving sign-up request through form in "/user/form.html"
 */
public class UserCreateController implements Controller {
    private final UserService userService = new UserService();
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        userService.updateUser(httpRequest.getBody());
        return new HttpResponseVersion1().setResponseCode(302)
                .setHeader("Location", "/index.html");
    }
}
