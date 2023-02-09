package logics.controller.get;

import logics.Service.SessionService;
import logics.controller.Controller;
import logics.Service.UserService;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.io.IOException;
import java.net.URISyntaxException;

import static logics.Service.HandlebarService.applyHandlebars;
import static logics.controller.support.RequestUtility.*;
import static utils.FileIoUtils.*;

/**
 * respond when receiving "GET /user/list"
 * In case of not logged-in, redirect to "/user/login"
 */
public class UserListController implements Controller {
    private final SessionService sessionService = new SessionService();
    private final UserService userService = new UserService();
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        if (!sessionService.isValidSessionKey(sessionService.parseSessionKey(httpRequest.getHeaderParameter("Cookie")))){
            return new HttpResponseVersion1().setResponseCode(302).setHeader("Location", "/user/login.html");
        }
        try {
            String modifiedURL = urlConverter(httpRequest.getURI().getPath());
            return new HttpResponseVersion1().setResponseCode(200)
                    .setHeader("Content-Type", getAppropriateContentType(httpRequest))
                    .setBody(applyHandlebars(loadFileFromClasspath(modifiedURL), userService.getUserInformation()));
        } catch(URISyntaxException | IOException | NullPointerException e){ // URI가 valid하지 않거나, URI가 null이라면
            return new DefaultResponseController().makeResponse(httpRequest);
        }
    }

}
