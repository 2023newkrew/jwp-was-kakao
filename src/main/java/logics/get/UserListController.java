package logics.get;

import logics.Controller;
import logics.ExecuteHandlebars;
import logics.Service;
import utils.FileIoUtils;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;
import utils.session.Session;
import utils.session.SessionManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class UserListController extends Controller {
    private final Service service = new Service();
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        String sessionId = service.parseSessionKey(httpRequest.getHeaderParameter("Cookie"));
        Session session = SessionManager.getManager.findSession(sessionId);
        if (Objects.isNull(session)){
            return new HttpResponseVersion1().setResponseCode(302).setHeader("Location", "/user/login.html");
        }

        try {
            String modifiedURL = urlConverter(httpRequest.getURI().getPath());
            return new HttpResponseVersion1().setResponseCode(200)
                    .setHeader("Content-Type", getAppropriateContentType(httpRequest))
                    .setBody(ExecuteHandlebars.converter(FileIoUtils.loadFileFromClasspath(modifiedURL), service.getUserInformation()));
        } catch(URISyntaxException | IOException | NullPointerException e){ // URI가 valid하지 않거나, URI가 null이거나 한다면
            e.printStackTrace();
            return new DefaultResponseController().makeResponse(httpRequest);
        }
    }

}
